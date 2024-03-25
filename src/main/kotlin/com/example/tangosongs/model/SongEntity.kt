package com.example.tangosongs.model

import io.hypersistence.utils.hibernate.type.search.PostgreSQLTSVectorType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.Type
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
    val trackId: String? = null,

    @Type(PostgreSQLTSVectorType::class)
    @Column(name = "singer_orchestra_name_search_vector", columnDefinition = "tsvector")
    val singerOrchestraNameSearchVector: String? = null
) {
}