package com.example.tangosongs.service

import com.example.tangosongs.model.SongEntity
import com.example.tangosongs.repository.SongRepository
import com.example.tangosongs.service.spotify.SpotifyClient
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class SpotifyService(
    private val songRepository: SongRepository,
    private val spotifyClient: SpotifyClient
) {

    @Scheduled(fixedDelay = 10000, initialDelay = 10000)
    fun fillTrackIds() {
        songRepository.findByTrackIdIsNull().map {
            val trackId = searchMatchingSpotifyTrackId(it)
            if (trackId != null) {
                songRepository.updateTrackId(it.id, trackId)
            }
        }
    }

    fun searchMatchingSpotifyTrackId(songEntity: SongEntity): String? {
        return spotifyClient.searchTracks("${songEntity.name} ${songEntity.orchestra}")
            ?.tracks?.items?.firstOrNull()?.id
    }
}