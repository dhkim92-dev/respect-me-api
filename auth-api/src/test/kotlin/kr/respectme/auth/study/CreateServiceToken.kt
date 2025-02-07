package kr.respectme.auth.study

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kr.respectme.auth.support.createJwtConfigs
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class CreateServiceToken {

    private val jwtConfigs = createJwtConfigs(accessTokenExpiry=3600*24*365*60)
    private val accessTokenAlgorithm = Algorithm.HMAC256(jwtConfigs.accessTokenSecretKey)

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
        println("Auth Service : ${token}")
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

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `파일 서비스 Access Token 생성`() {
        val token = JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject("0194e02c-8cef-70bf-a45b-ec1501f7c679")
            .withClaim("email", "file_service@respect-me.kr")
            .withClaim("nickname", "file_service")
            .withArrayClaim("roles", arrayOf("ROLE_SERVICE"))
            .withClaim("isActivated", true)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(jwtConfigs.accessTokenExpiry*1000L))
            .sign(accessTokenAlgorithm)
        println("Message Service Access token : ${token}")
        println("RESPECT_ME_SERVICE_ACCOUNT_TOKEN: ${Base64.Default.encode(token.encodeToByteArray())}")
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `신고 서비스 Access Token 생성`() {
        val token = JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject("0194e02e-5a79-77f0-90df-03d6e7bab1b6")
            .withClaim("email", "report_service@respect-me.kr")
            .withClaim("nickname", "report_service")
            .withArrayClaim("roles", arrayOf("ROLE_SERVICE"))
            .withClaim("isActivated", true)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(jwtConfigs.accessTokenExpiry*1000L))
            .sign(accessTokenAlgorithm)
        println("Message Service Access token : ${token}")
        println("RESPECT_ME_SERVICE_ACCOUNT_TOKEN: ${Base64.Default.encode(token.encodeToByteArray())}")
    }

    private fun createMemberAccessToken(member: TestMember) {
        val token = JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject(member.id.toString())
            .withClaim("email", member.email)
            .withClaim("nickname", member.email)
            .withArrayClaim("roles", arrayOf(member.role))
            .withClaim("isActivated", true)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(jwtConfigs.accessTokenExpiry*1000L))
            .sign(accessTokenAlgorithm)
        println("${member.email} Access token : ${token}")
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `유저 별 Token 발급`() {
        val members = listOf(
        TestMember.of("01917f7a-3d9f-75aa-be6b-ef76d583653b", "admin@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 2),
        TestMember.of("01917f7b-0355-7bd1-ae89-16f61bf7405c", "member-service@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01917f7b-28a5-7dce-9f54-5ef9349c6ef3", "auth-service@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01917f7a-aeeb-7d60-893b-e4c7c60ff9b9", "member1@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01917f7a-d55a-769e-9205-c328897c2fbf", "member2@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01918043-8f6f-7930-a283-f84ded99d409", "member3@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01918043-9105-755b-9940-ab9a24f0bb7d", "member4@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01918043-eb60-7300-9487-4fa0366b5f2a", "member5@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01918043-ed15-7ac9-98b9-c0b67ac717bc", "member6@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01918043-eed7-7705-99c2-c3903babdce0", "member7@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01918050-b1e6-7cd3-8ad4-476167dac987", "member8@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01918050-b415-7b50-b5e1-bde5b7873811", "member9@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3),
        TestMember.of("01918050-b60f-713a-aa40-2690654ae405", "member10@respect-me.kr", "$2b$12\$W73CKCBO927PQ/LOzVlX2uKwxJQsbHRbZwt7Y1f6miKfuAOThQrzG", 3))
        members.forEach { createMemberAccessToken(it) }
    }

    class TestMember(
        val id : UUID,
        val email: String,
        val password: String,
        val role: String
    ) {
        companion object {
            fun of(
                id: String,
                email: String,
                password: String,
                role: Int
            ): TestMember {
                return TestMember(
                    id = UUID.fromString(id),
                    email = email,
                    password = password,
                    role = if(role == 2) "ROLE_ADMIN" else "ROLE_MEMBER"
                )
            }
        }
    }
}