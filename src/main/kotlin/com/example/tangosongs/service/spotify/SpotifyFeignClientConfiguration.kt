package com.example.tangosongs.service.spotify

import com.example.tangosongs.service.OAuthFeignRequestInterceptor
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository


@Configuration
@EnableConfigurationProperties
class SpotifyFeignClientConfiguration {

    companion object {
        const val SPOTIFY_SERVICE_CLIENT_REGISTRATION_ID = "spotify"
    }

    @Bean
    fun spotifyFeignRequestInterceptor(
        oauth2AuthorizedClientManager: OAuth2AuthorizedClientManager,
        clientRegistrationRepository: ClientRegistrationRepository
    ): OAuthFeignRequestInterceptor {
        return OAuthFeignRequestInterceptor(
            oauth2AuthorizedClientManager,
            clientRegistrationRepository.findByRegistrationId(SPOTIFY_SERVICE_CLIENT_REGISTRATION_ID)
        )
    }

}