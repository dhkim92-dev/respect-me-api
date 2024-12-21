package kr.respectme.auth.common.oidc

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
@Qualifier("apple_jwks_provider")
class AppleJwksProvider(
    webClient: WebClient,
    objectMapper: ObjectMapper
): AbstractJwksProvider(webClient, objectMapper) {

    override val jwksUrl: String = "https://appleid.apple.com/auth/keys"
}