package kr.respectme.auth.unit.application.oidc

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.auth.application.oidc.AppleJwksProvider
import kr.respectme.auth.application.oidc.JWKKey
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.reactive.function.client.WebClient

class AppleJwksProviderTest: AnnotationSpec() {

    private val webClient = WebClient.create()

    private val objectMapper = Jackson2ObjectMapperBuilder.json().build<ObjectMapper>()

    private val appleJwksProvider = AppleJwksProvider(webClient, objectMapper)

    private val sampleJwkKey = JWKKey(
        kty="RSA",
        kid="dMlERBaFdK",
        use="sig",
        alg="RS256",
        n="ryLWkB74N6SJNRVBjKF6xKMfP-QW3AAsJotv0LjVtf7m4NZNg_gTL78e7O8wmvngF8FuzBrvqf1mGW17Ct8BgNK6YXxnoGL0YLmlwXbmCZvTXki0VlEW1PDXeViWy7qXaCp2caF5v4OOdPsgroxNO_DgJRTuA_izJ4DFZYHCHXwojfdWJiDYG67j5PlD5pXKGx7zaqyryjovZTEII_Z1_bhFCRUZRjfJ3TVoK0fZj2z7iAZWjn33i-V3zExUhwzEyeuGph0118NfmOLCUEy_Jd4xvLf_X4laPpe9nq8UeORfs72yz2qH7cHDKL85W6oG08Gu05JWuAs5Ay49WxJrmw",
        e="AQAB"
    )

    @Test
    fun `createPublicKey Test`() {
        val publicKey = appleJwksProvider.getPublicKey(sampleJwkKey.kid)
        publicKey.algorithm shouldBe "RSA"
    }
}