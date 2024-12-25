package kr.respectme.auth.application.oidc

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
@Qualifier("google_jwks_provider")
class GoogleJwksProvider(
    webClient: WebClient,
    objectMapper: ObjectMapper
): AbstractJwksProvider(webClient, objectMapper) {

    override val jwksUrl: String = "https://www.googleapis.com/oauth2/v3/certs"
}