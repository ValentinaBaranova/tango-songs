package com.example.tangosongs.controller

import com.example.tangosongs.api.model.SongDto
import com.example.tangosongs.service.SongService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SongController(
    val songService: SongService
) {

    companion object {
        const val API_PREFIX = "/v1/songs"
    }

    @GetMapping(API_PREFIX)
    @Operation(description = "Get list of songs")
    fun getSongs(): List<SongDto> {
        return songService.getSongs().map {
            SongDto(
                name = it.name,
                orchestra = it.orchestra,
                singer = it.singer,
                releaseYear = it.releaseYear,
                trackId = it.trackId,
            )
        }
    }
}