package com.example.tangosongs.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "song")
data class SongEntity(
    @Id
    val id: UUID,
    val name: String?,
    val orchestra: String?,
    val singer: String? = null,
    val releaseYear: String? = null,
    val trackId: String? = null
) {
}