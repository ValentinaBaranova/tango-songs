package com.example.tangosongs.api.model

import java.time.LocalDate

data class SongDto (
    val name: String?,
    val orchestra: String?,
    val singers: String?,
    val releaseDate: LocalDate?,
    val trackId: String?
)