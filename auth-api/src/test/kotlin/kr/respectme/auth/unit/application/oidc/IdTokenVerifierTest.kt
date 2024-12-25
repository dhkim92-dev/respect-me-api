package kr.respectme.auth.unit.application.oidc

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.respectme.auth.application.oidc.AppleJwksProvider
import kr.respectme.auth.application.oidc.GoogleJwksProvider
import kr.respectme.auth.application.oidc.IdTokenVerifier
import kr.respectme.auth.support.createRSAKeyPair
import kr.respectme.auth.support.createSampleAppleIdToken
import kr.respectme.auth.support.createSampleGoogleIdToken
import java.security.interfaces.RSAPublicKey

class IdTokenVerifierTest: AnnotationSpec() {

    private val googleJwksProvider: GoogleJwksProvider = mockk()
    private val appleJwksProvider: AppleJwksProvider = mockk()
    private val idTokenVerifier = IdTokenVerifier(googleJwksProvider, appleJwksProvider)

    @Test
    fun `Apple Id Token을 검증한다`() {
        // given
        val keyPair = createRSAKeyPair()
        val idToken = createSampleAppleIdToken(keyPair)
        println(idToken)
        every { appleJwksProvider.getPublicKey( any() ) } returns (keyPair.public as RSAPublicKey)

        // when
        val payload = idTokenVerifier.verifyToken(idToken)

        // then
        payload.iss shouldBe "https://appleid.apple.com"
    }

    @Test
    fun `Google Id Token을 검증한다`() {
        // given
        val keyPair = createRSAKeyPair()
        val idToken = createSampleGoogleIdToken(
            keyPair = keyPair,
        )

        every { googleJwksProvider.getPublicKey( any() ) } returns (keyPair.public as RSAPublicKey)

        // when
        val payload = idTokenVerifier.verifyToken(idToken)

        // then
        payload.iss shouldBe "https://accounts.google.com"
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }
}