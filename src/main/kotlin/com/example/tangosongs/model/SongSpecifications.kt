package com.example.tangosongs.model

import org.springframework.data.jpa.domain.Specification

class SongSpecifications {
    companion object {

        fun search(search: String): Specification<SongEntity> {
            return Specification<SongEntity> { root, query, cb ->
                if (search.isBlank()) return@Specification null
                cb.isTrue(
                    cb.function(
                        "tsvector_match",
                        Boolean::class.java,
                        root.get<String>("singersOrchestraNameSearchVector"),
                        cb.function("plainto_tsquery", String::class.java, cb.literal("spanish"), cb.literal(search))
                    )
                )
            }
        }
    }
}