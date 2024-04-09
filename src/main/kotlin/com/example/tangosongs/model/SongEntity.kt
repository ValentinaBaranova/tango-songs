package com.example.tangosongs.model

import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Generated
import org.hibernate.annotations.Type
import java.time.Instant
import java.util.*

@Entity
@Table(name = "song")
class SongEntity(
    @Id
    val id: UUID,
    val name: String,
    val orchestra: String,
    val singers: String? = null,
    val releaseYear: Int? = null,
    val trackId: String? = null,
    val trackIdRequestedAt: Instant? = null,
    val sourceFilename: String? = null,
    val sourceLoadedAt: Instant? = null,

    @Type(PostgreSQLTSVectorType::class)
    @Column(name = "singers_orchestra_name_search_vector", columnDefinition = "tsvector")
    @Generated
    val singersOrchestraNameSearchVector: String? = null
)