package kr.respectme.auth.application.adapter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import kr.respectme.auth.application.dto.AuthenticationResult
import kr.respectme.auth.application.dto.TokenValidationResult
import kr.respectme.auth.application.port.AuthUseCase
import kr.respectme.auth.common.AuthenticationErrorCode
import kr.respectme.auth.configs.JwtConfigs
import kr.respectme.auth.infrastructures.dto.LoginRequest
import kr.respectme.auth.infrastructures.dto.Member
import kr.respectme.auth.infrastructures.ports.MemberLoadPort
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.error.UnauthorizedException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Service
class AuthService(
    private val jwtConfigs: JwtConfigs,
    private val memberLoadPort: MemberLoadPort
): AuthUseCase {

    private val accessTokenAlgorithm = Algorithm.HMAC512(jwtConfigs.accessTokenSecretKey)
    private val refreshTokenAlgorithm = Algorithm.HMAC512(jwtConfigs.refreshTokenSecretKey)
    private val accessTokenVerifier = JWT.require(Algorithm.HMAC512(jwtConfigs.accessTokenSecretKey))
        .withIssuer(jwtConfigs.issuer)
        .build()
    private val refreshTokenVersion = JWT.require(Algorithm.HMAC512(jwtConfigs.refreshTokenSecretKey))
        .withIssuer(jwtConfigs.issuer)
        .build()
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun login(loginRequest: LoginRequest): AuthenticationResult {
        logger.debug("login request: ${loginRequest}")
        val member = memberLoadPort.loadMemberByEmailAndPassword(loginRequest).data
            ?: throw UnauthorizedException(AuthenticationErrorCode.FAILED_TO_SIGN_IN)

        val refreshToken = createRefreshToken(member.id)
        val accessToken = createAccessToken(member)

        return AuthenticationResult(
            type = "Bearer",
            refreshToken = refreshToken,
            accessToken = accessToken,
        )
    }

    override fun validateToken(token: String): TokenValidationResult {
        try {
            val decoded = accessTokenVerifier.verify(token)
            val instant = Instant.ofEpochMilli(decoded.expiresAt.time)
            return TokenValidationResult(
                issuer = decoded.issuer,
                type = "Bearer",
                memberId = UUID.fromString(decoded.subject),
                roles = decoded.claims["roles"]?.asArray(String::class.java) ?: emptyArray(),
                email = decoded.claims["email"]?.asString() ?: "",
                nickname = decoded.claims["nickname"]?.asString() ?: "",
                isActivated = decoded.claims["isActivated"]?.asBoolean() ?: false,
                expiresAt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()),
            )

        } catch (e: Exception) {
            when (e) {
                is TokenExpiredException -> throw UnauthorizedException(AuthenticationErrorCode.EXPIRED_ACCESS_TOKEN)
                else -> throw UnauthorizedException(AuthenticationErrorCode.INVALID_ACCESS_TOKEN)
            }
        }
    }

    override fun refreshAccessToken(refreshToken: String): AuthenticationResult {
        // TODO 추후 member-api를 통해 사용자 정보를 얻어 온 후 사용자 정보를 기반으로 토큰을 발급해야 한다.
        try {
            val decoded = refreshTokenVersion.verify(refreshToken)
            val member = memberLoadPort.loadMemberById(UUID.fromString(decoded.subject)).data
                ?: throw NotFoundException(AuthenticationErrorCode.INVALID_REFRESH_TOKEN)
            return AuthenticationResult(
                type = "Bearer",
                refreshToken = refreshToken,
                accessToken = createAccessToken(member)
            )
        } catch (e: Exception) {
            when (e) {
                is TokenExpiredException -> throw UnauthorizedException(AuthenticationErrorCode.EXPIRED_REFRESH_TOKEN)
                else -> throw UnauthorizedException(AuthenticationErrorCode.INVALID_REFRESH_TOKEN)
            }
        }
    }

    private fun createAccessToken(member: Member): String {
        val now = Instant.now()
        val expiry = if(member.role == "ROLE_SERVICE") {
            now.plusMillis(365L*24L*60L*60L*1000L)
        } else {
            now.plusMillis(jwtConfigs.accessTokenExpiry)
        }

        return JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject(member.id.toString())
            .withClaim("email", member.email)
            .withClaim("nickname", member.nickname)
            .withArrayClaim("roles", arrayOf(member.role))
            .withClaim("isActivated", !member.isBlocked)
            .withExpiresAt(now.plusMillis(jwtConfigs.accessTokenExpiry))
            .withIssuedAt(Instant.now())
            .sign(accessTokenAlgorithm)
    }

    private fun createRefreshToken(memberId: UUID): String {
        val now = Instant.now()
        return JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject(memberId.toString())
            .withExpiresAt(now.plusMillis(jwtConfigs.refreshTokenExpiry))
            .withIssuedAt(now)
            .sign(refreshTokenAlgorithm)
    }
}