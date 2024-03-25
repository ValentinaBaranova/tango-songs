package com.example.tangosongs.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfiguration {

    @Bean
    fun oauth2AuthorizedClientManager(
        clientRegistrationRepository: ClientRegistrationRepository,
        oauth2AuthorizedClientService: OAuth2AuthorizedClientService
    ): OAuth2AuthorizedClientManager {
        return AuthorizedClientServiceOAuth2AuthorizedClientManager(
            clientRegistrationRepository,
            oauth2AuthorizedClientService
        )
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable().oauth2Client {  } }

        http
            .authorizeHttpRequests { it.anyRequest().permitAll() }

        return http.build()
    }
}