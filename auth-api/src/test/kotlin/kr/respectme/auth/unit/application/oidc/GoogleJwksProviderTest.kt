package kr.respectme.auth.unit.application.oidc

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kr.respectme.auth.application.oidc.GoogleJwksProvider
import kr.respectme.auth.application.oidc.JWKKey
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.reactive.function.client.WebClient

class GoogleJwksProviderTest: AnnotationSpec() {

    private val webClient = WebClient.create()
    private val objectMapper = Jackson2ObjectMapperBuilder.json().build<ObjectMapper>()

    private val googleJwksProvider = GoogleJwksProvider(webClient, objectMapper)
    private val sampleJwkKey = JWKKey(
        kid = "31b8fccb2e52253b133138acf0e56632f09957ee",
        kty = "RSA",
        alg = "RS256",
        e = "AQAB",
        use = "sig",
        n ="qL80q4yfbwG9vt_x1CBgv51oMOlOV1nEIWxcPrEJ_hd1Zf6Tv-gGNQUTzdRqhWUB7VZbIe7IGQ8XrqqZhJkSSRutWYgcB7CZAQPsz2uUzJfULIrqU5-3s1V6TsAvDd0XAhTrxsukBZhvSUcObns6oyr2tvCeYkdlbZ7HHgUjGLt2JduwfVfSDVOXm9iev36W0cDv2RS45H7c4rBaXnvhMQ6BOMA8xeSI05SCwYGpZp5prgQE_xyBPB_EHBOheDOgdtOEvceGg4zMSRni7a5S0ux5EmTOUVxMXOdOzlLmBHMNPYpAjjgYz4afhNuwAlp0BhEhSWwINOGh22U8iU1pIQ"
    )

    @Test
    fun `createPublicKey Test`() {
        val publicKey = googleJwksProvider.getPublicKey(sampleJwkKey.kid)
        publicKey.algorithm shouldBe "RSA"
    }

}