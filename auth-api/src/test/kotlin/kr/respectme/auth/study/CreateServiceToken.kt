package kr.respectme.auth.study

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kr.respectme.auth.support.createJwtConfigs
import org.junit.jupiter.api.Test
import org.springframework.security.config.annotation.rsocket.RSocketSecurity.JwtSpec
import java.time.Instant
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CreateServiceToken {

    private val jwtConfigs = createJwtConfigs(accessTokenExpiry=3600*24*365*60)
    private val accessTokenAlgorithm = Algorithm.HMAC512(jwtConfigs.accessTokenSecretKey)

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `인증 서비스 Access Token 생성`() {
        val token = JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject("01917f7b-28a5-7dce-9f54-5ef9349c6ef3")
            .withClaim("email", "auth-service@respect-me.kr")
            .withClaim("nickname", "auth_service")
            .withArrayClaim("roles", arrayOf("ROLE_SERVICE"))
            .withClaim("isActivated", true)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(jwtConfigs.accessTokenExpiry*1000L))
            .sign(accessTokenAlgorithm)
        println("Auth Service Access token : ${token}")
        println("RESPECT_ME_SERVICE_ACCOUNT_TOKEN: ${Base64.Default.encode(token.encodeToByteArray())}")
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `멤버 서비스 Access Token 생성`() {
        val token = JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject("01917f7b-0355-7bd1-ae89-16f61bf7405c")
            .withClaim("email", "member-service@respect-me.kr")
            .withClaim("nickname", "member_service")
            .withArrayClaim("roles", arrayOf("ROLE_SERVICE"))
            .withClaim("isActivated", true)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(jwtConfigs.accessTokenExpiry*1000L))
            .sign(accessTokenAlgorithm)
        println("Member Service Access token : ${token}")
        println("RESPECT_ME_SERVICE_ACCOUNT_TOKEN: ${Base64.Default.encode(token.encodeToByteArray())}")
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `그룹 서비스 Access Token 생성`() {
        val token = JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject("019290ea-4f42-78ab-8a90-a4ee3270a9bb")
            .withClaim("email", "group-service@respect-me.kr")
            .withClaim("nickname", "group_service")
            .withArrayClaim("roles", arrayOf("ROLE_SERVICE"))
            .withClaim("isActivated", true)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(jwtConfigs.accessTokenExpiry*1000L))
            .sign(accessTokenAlgorithm)
        println("Group Service Access token : ${token}")
        println("RESPECT_ME_SERVICE_ACCOUNT_TOKEN: ${Base64.Default.encode(token.encodeToByteArray())}")
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `메시지 서비스 Access Token 생성`() {
        val token = JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject("0192995d-d314-78cc-930f-d61247bf329f")
            .withClaim("email", "message_service@respect-me.kr")
            .withClaim("nickname", "message_service")
            .withArrayClaim("roles", arrayOf("ROLE_SERVICE"))
            .withClaim("isActivated", true)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(jwtConfigs.accessTokenExpiry*1000L))
            .sign(accessTokenAlgorithm)
        println("Message Service Access token : ${token}")
        println("RESPECT_ME_SERVICE_ACCOUNT_TOKEN: ${Base64.Default.encode(token.encodeToByteArray())}")
    }
}