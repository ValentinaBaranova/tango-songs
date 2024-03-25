package com.example.tangosongs.service

import com.example.tangosongs.AbstractTest
import com.example.tangosongs.model.SongEntity
import com.example.tangosongs.repository.SongRepository
import com.example.tangosongs.service.spotify.SearchResponse
import com.example.tangosongs.service.spotify.SpotifyClient
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlin.jvm.optionals.getOrNull

class SpotifyServiceTest : AbstractTest() {

    @Autowired
    lateinit var spotifyClient: SpotifyClient

    @Autowired
    lateinit var spotifyService: SpotifyService

    @Autowired
    lateinit var songRepository: SongRepository

    @Test
    fun `should fill trackId if track is found`() {
        val songId = UUID.randomUUID()
        songRepository.save(SongEntity(
            id = songId,
            name = "Testname",
            orchestra = "Testorchestra"
        ))
        whenever(spotifyClient.searchTracks(eq("Testname Testorchestra"))).thenReturn(
            SearchResponse(
                tracks = SearchResponse.Tracks(
                    items = listOf(
                        SearchResponse.Item(
                            id = UUID.randomUUID().toString()
                        )
                    )
                )
            )
        )
        spotifyService.fillTrackIds()
        val updatedSong = songRepository.findById(songId).getOrNull()
        assertThat(updatedSong).isNotNull
        assertThat(updatedSong!!.trackId).isNotNull()
    }

    @Test
    fun `should not fill trackId if track is not found`() {
        val songId = UUID.randomUUID()
        songRepository.save(SongEntity(
            id = songId,
            name = "Testname2",
            orchestra = "Testorchestra2"
        ))
        spotifyService.fillTrackIds()
        val updatedSong = songRepository.findById(songId).getOrNull()
        assertThat(updatedSong).isNotNull
        assertThat(updatedSong!!.trackId).isNull()
    }

}