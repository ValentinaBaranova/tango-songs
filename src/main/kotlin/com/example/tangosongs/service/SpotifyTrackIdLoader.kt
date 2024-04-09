package com.example.tangosongs.service

import com.example.tangosongs.model.SongEntity
import com.example.tangosongs.repository.SongRepository
import com.example.tangosongs.service.spotify.SpotifyClient
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Limit
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.TimeUnit

@Component
class SpotifyTrackIdLoader(
    private val songRepository: SongRepository,
    private val spotifyClient: SpotifyClient,
    @Value("\${application.scheduling.task.spotify-task.batch-size}")
    private val batchSize: Int,
    @Value("\${application.scheduling.task.spotify-task.policy-delay-ms}")
    private val policyDelay: Long,
    @Value("\${application.scheduling.task.spotify-task.load-error-expired-duration}")
    private val loadErrorExpiredDuration: Duration
) {

    private val log = KotlinLogging.logger { }

    fun fillTrackIds() {
        var songsToProcess = songRepository.findWithoutTrackId(
            Limit.of(batchSize), Instant.now().minus(loadErrorExpiredDuration.toMillis(), ChronoUnit.MILLIS)
        )
        while (songsToProcess.isNotEmpty()) {
            log.info { "Starting loading spotify trackId..." }
            songsToProcess.map {
                try {
                    val trackId = searchMatchingSpotifyTrackId(it)
                    if (trackId != null) {
                        songRepository.updateTrackId(it.id, trackId)
                    } else {
                        songRepository.updateTrackIdRequestedAt(it.id)
                    }
                } catch (e: RuntimeException) {
                    log.error(e) { "Can't get trackId for song ${it.name} from spotify because of error" }
                    songRepository.updateTrackIdRequestedAt(it.id)
                }
            }
            log.info { "Processed ${songsToProcess.size}" }
            TimeUnit.MILLISECONDS.sleep(policyDelay)
            songsToProcess = songRepository.findWithoutTrackId(
                Limit.of(batchSize), Instant.now().minus(loadErrorExpiredDuration.toMillis(), ChronoUnit.MILLIS)
            )
        }
    }

    private fun searchMatchingSpotifyTrackId(songEntity: SongEntity): String? {
        return spotifyClient.searchTracks("${songEntity.name} ${songEntity.orchestra}")
            ?.tracks?.items?.firstOrNull()?.id
    }

}