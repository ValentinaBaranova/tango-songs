package com.example.tangosongs.service.loader

import com.example.tangosongs.AbstractTest
import com.example.tangosongs.repository.SongRepository
import com.example.tangosongs.service.SongMetadataLoader
import com.example.tangosongs.service.SongService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable

class SongMetadataLoaderTest: AbstractTest() {

    @Autowired
    lateinit var songMetadataLoader: SongMetadataLoader

    @Autowired
    lateinit var songRepository: SongRepository

    @Autowired
    lateinit var songService: SongService

    @Test
    fun extractSongMetadata() {
        songMetadataLoader.loadSongs()
        val songsWasLoaded = songRepository.existsBySourceFilenameIsNotNull()
        assertThat(songsWasLoaded).isTrue()
        val song = songService.getSongs("Adios", Pageable.unpaged()).firstOrNull()
        assertThat(song).isNotNull
        assertThat(song!!.name).isEqualTo("Adios")
        assertThat(song.orchestra).isEqualTo("Miguel Calo")
        assertThat(song.singers).isEqualTo("Alberto Marino")
        assertThat(song.releaseYear).isEqualTo(1967)
    }
}