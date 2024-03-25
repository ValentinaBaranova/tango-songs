package com.example.tangosongs.service.spotify

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SearchResponse(
    val tracks: Tracks?
) {
    data class Tracks(
        val items: List<Item>
    )

    data class Item(
        val id: String
    )
}