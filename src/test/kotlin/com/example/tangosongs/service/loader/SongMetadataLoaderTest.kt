package com.example.tangosongs.service.loader

import com.example.tangosongs.AbstractTest
import com.example.tangosongs.repository.SongRepository
import com.example.tangosongs.service.SongMetadataLoader
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class SongMetadataLoaderTest: AbstractTest() {

    @Autowired
    lateinit var songMetadataLoader: SongMetadataLoader

    @Autowired
    lateinit var songRepository: SongRepository

    @Test
    fun extractSongMetadata() {
        songMetadataLoader.loadSongs()
        val songs = songRepository.findBySourceFilename("01-Cantando(04-09-1931).mp3")
        assertThat(songs.size).isEqualTo(1)
    }
}