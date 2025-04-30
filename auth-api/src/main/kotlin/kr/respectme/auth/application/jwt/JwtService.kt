package kr.respectme.auth.application.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import kr.respectme.auth.common.AuthenticationErrorCode
import kr.respectme.auth.configs.JwtConfigs
import kr.respectme.auth.port.out.persistence.member.dto.Member
import kr.respectme.common.error.UnauthorizedException
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.*

@Service
class JwtService(
    private val jwtConfigs: JwtConfigs,
) {
    private val accessTokenAlgorithm = Algorithm.HMAC256(jwtConfigs.accessTokenSecretKey)
    private val refreshTokenAlgorithm = Algorithm.HMAC256(jwtConfigs.refreshTokenSecretKey)
    private val accessTokenVerifier = JWT.require(Algorithm.HMAC256(jwtConfigs.accessTokenSecretKey))
        .withIssuer(jwtConfigs.issuer)
        .build()
    private val refreshTokenVerifier = JWT.require(Algorithm.HMAC256(jwtConfigs.refreshTokenSecretKey))
        .withIssuer(jwtConfigs.issuer)
        .build()

    fun createAccessToken(member: Member): String {
        val now = Instant.now()
        val expiry = if(member.role == "ROLE_SERVICE") {
            now.plus(Duration.ofMillis(365L*24L*60L*60L*1000L))
        } else {
            now.plus(Duration.ofMillis(jwtConfigs.accessTokenExpiry))
        }

        return JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject(member.id.toString())
            .withClaim("email", member.email)
            .withArrayClaim("roles", arrayOf(member.role))
            .withClaim("isActivated", !member.isBlocked)
            .withExpiresAt(expiry)
            .withIssuedAt(now)
            .sign(accessTokenAlgorithm)
    }

    fun createRefreshToken(memberId: UUID): String {
        val now = Instant.now()
        return JWT.create()
            .withIssuer(jwtConfigs.issuer)
            .withSubject(memberId.toString())
            .withExpiresAt(now.plus(Duration.ofMillis(jwtConfigs.refreshTokenExpiry)))
            .withIssuedAt(now)
            .sign(refreshTokenAlgorithm)
    }

    fun verifyRefreshToken(token: String): DecodedJWT {
        return try {
            refreshTokenVerifier.verify(token)
        } catch(e: JWTVerificationException) {
            throw UnauthorizedException(AuthenticationErrorCode.INVALID_REFRESH_TOKEN)
        }
    }

    fun verifyAccessToken(token: String): DecodedJWT {
        return try {
            accessTokenVerifier.verify(token)
        } catch(e: JWTVerificationException) {
            throw UnauthorizedException(AuthenticationErrorCode.INVALID_ACCESS_TOKEN)
        }
    }
}