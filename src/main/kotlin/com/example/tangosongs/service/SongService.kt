package com.example.tangosongs.service

import com.example.tangosongs.api.model.SongDto
import com.example.tangosongs.model.SongSpecifications
import com.example.tangosongs.repository.SongRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class SongService(
    private val songRepository: SongRepository,
) {

    fun getSongs(q: String, pageable: Pageable): Page<SongDto> {
        return songRepository.findAll(SongSpecifications.search(q), pageable).map {
            SongDto(
                name = it.name,
                orchestra = it.orchestra,
                singers = it.singers,
                releaseYear = it.releaseYear,
                trackId = it.trackId,
            )
        }
    }

}