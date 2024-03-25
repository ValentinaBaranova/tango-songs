package com.example.tangosongs.service

import com.example.tangosongs.AbstractTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class SongServiceTest: AbstractTest() {

    @Autowired
    lateinit var songService: SongService

    @Test
    fun getSongs() {
        val songs = songService.getSongs("Cumparsita")
        assertThat(songs.size).isEqualTo(1)
    }
}