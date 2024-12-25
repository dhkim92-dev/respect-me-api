package kr.respectme.auth.unit.application.jwt

import com.auth0.jwt.exceptions.JWTVerificationException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.respectme.auth.application.jwt.JwtService
import kr.respectme.auth.infrastructures.dto.Member
import kr.respectme.auth.support.createJwtConfigs
import kr.respectme.common.error.UnauthorizedException
import java.util.UUID

class JwtServiceTest : AnnotationSpec() {

    private val jwtConfig = createJwtConfigs(accessTokenExpiry = 60000, refreshTokenExpiry = 60000)

    private val jwtService = JwtService(jwtConfig)

    private lateinit var member: Member;

    private lateinit var expiredAccessToken: String

    private lateinit var expiredRefreshToken: String

    @BeforeEach
    fun setUp() {
        member = Member(
            id = UUID.randomUUID(),
            email = "nickname@respect-me.kr",
            role = "ROLE_MEMBER",
            isBlocked = false,
            blockReason = ""
        )

        val tmpService = JwtService(createJwtConfigs(accessTokenExpiry = 1, refreshTokenExpiry = 1))
        expiredAccessToken = tmpService.createAccessToken(member)
        expiredRefreshToken = tmpService.createRefreshToken(member.id)
    }

    @Test
    fun `멤버 객체가 주어지면 Access Token이 생성된다`() {
        // given
        val member = this.member

        //When
        val accessToken = jwtService.createAccessToken(member)
        val decodedJwt = jwtService.verifyAccessToken(accessToken)
        //Then
        accessToken shouldNotBe null
        decodedJwt.subject shouldBe member.id.toString()
        decodedJwt.getClaim("email").asString() shouldBe member.email
        decodedJwt.getClaim("roles").asList(String::class.java) shouldBe listOf(member.role)
        decodedJwt.getClaim("isActivated").asBoolean() shouldBe !member.isBlocked
    }

    @Test
    fun `멤버 객체가 주어지면 Refresh Token이 생성된다`() {
        // given
        val memberId = member.id

        //When
        val refreshToken = jwtService.createRefreshToken(memberId)
        val decodedJwt = jwtService.verifyRefreshToken(refreshToken)
        //Then
        refreshToken shouldNotBe null
        decodedJwt.subject shouldBe memberId.toString()
    }

    @Test
    fun `만료된 Access Token을 검증하면 UnauthorizedException이 발생한다`() {
        // given
        val expiredAccessToken = this.expiredAccessToken

        //When
        val exception = shouldThrowAny {
            jwtService.verifyAccessToken(expiredAccessToken)
        }

        //Then
        exception::class.java shouldBe UnauthorizedException::class.java
    }

    @Test
    fun `만료된 Refresh Token을 검증하면 UnauthorizedException이 발생한다`() {
        // given
        val expiredRefreshToken = this.expiredRefreshToken

        //When
        val exception = shouldThrowAny {
            jwtService.verifyRefreshToken(expiredRefreshToken)
        }

        //Then
        exception::class.java shouldBe UnauthorizedException::class.java
    }
}