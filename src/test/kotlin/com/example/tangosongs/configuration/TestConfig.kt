package com.example.tangosongs.configuration

import com.example.tangosongs.service.spotify.SpotifyClient
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class TestConfig {

    @Bean
    @Primary
    fun mockSpotifyClient(): SpotifyClient {
        return Mockito.mock(SpotifyClient::class.java)
    }
}