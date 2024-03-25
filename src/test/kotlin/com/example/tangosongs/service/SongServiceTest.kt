package com.example.tangosongs.service

import com.example.tangosongs.AbstractTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable

class SongServiceTest: AbstractTest() {

    @Autowired
    lateinit var songService: SongService

    @Test
    fun getSongs() {
        val songs = songService.getSongs("cabeza gardel", Pageable.unpaged())
        assertThat(songs.size).isEqualTo(1)
    }
}