package com.example.tangosongs.service

import com.example.tangosongs.model.SongEntity
import com.example.tangosongs.repository.SongRepository
import org.springframework.stereotype.Service

@Service
class SongService(
    private val songRepository: SongRepository
) {

    fun getSongs(): MutableList<SongEntity> {
        return songRepository.findAll()
    }

}