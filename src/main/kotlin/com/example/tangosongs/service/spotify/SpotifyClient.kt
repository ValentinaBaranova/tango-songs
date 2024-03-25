package com.example.tangosongs.service.spotify

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "spotify-client",
    url = "\${feign.client.config.spotify.url}",
    primary = false,
    configuration = [SpotifyFeignClientConfiguration::class]
)
interface SpotifyClient {

    @GetMapping("/v1/search?type=track")
    fun searchTracks(
        @RequestParam("q") query: String
    ): SearchResponse?
}