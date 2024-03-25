package com.example.tangosongs.service

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.core.OAuth2AccessToken
import java.util.*


class OAuthFeignRequestInterceptor(
    private val oauth2AuthorizedClientManager: OAuth2AuthorizedClientManager,
    private val clientRegistration: ClientRegistration
) : RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        template.header("Authorization", getAuthorizationToken())
    }

    private fun getAuthorizationToken(): String {
        val accessToken: String = getAccessToken()
        return java.lang.String.format("Bearer %s", accessToken)
    }

    private fun getAccessToken(): String {
        val authorizedClient: OAuth2AuthorizedClient = authorize()

        return Optional.ofNullable(authorizedClient)
            .map { obj: OAuth2AuthorizedClient -> obj.accessToken }
            .map { obj: OAuth2AccessToken -> obj.tokenValue }
            .orElseThrow {
                IllegalStateException("Failed to get access token")
            }
    }

    private fun authorize(): OAuth2AuthorizedClient {
        val authorizeRequest: OAuth2AuthorizeRequest = OAuth2AuthorizeRequest
            .withClientRegistrationId(clientRegistration.registrationId)
            .principal(clientRegistration.clientId)
            .build()

        return oauth2AuthorizedClientManager.authorize(authorizeRequest) ?: error("Can't perform authorize request")
    }
}
