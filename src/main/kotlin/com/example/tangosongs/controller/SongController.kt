package com.example.tangosongs.controller

import com.example.tangosongs.api.model.SongDto
import com.example.tangosongs.service.SongService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import org.springdoc.core.converters.models.PageableAsQueryParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SongController(
    val songService: SongService
) {

    companion object {
        const val API_PREFIX = "/v1/songs"
        const val DEFAULT_PAGE_SIZE = 20
    }

    @Operation(description = "Get list of songs")
    @GetMapping(API_PREFIX)
    @PageableAsQueryParam
    fun getSongs(
        @RequestParam
        q: String,
        @Parameter(hidden = true) pageable: Pageable
    ): Page<SongDto> {
        return songService.getSongs(q, pageable)
    }
}