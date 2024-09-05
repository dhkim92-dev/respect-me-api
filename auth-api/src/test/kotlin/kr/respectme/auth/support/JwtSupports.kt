package kr.respectme.auth.support

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kr.respectme.auth.configs.JwtConfigs
import kr.respectme.auth.infrastructures.dto.Member
import java.util.*

fun createJwtConfigs(
    accessTokenExpiry : Long = 0,
    refreshTokenExpiry : Long = 0,
    issuer : String = "https://identifier.respect-me.kr",
    accessTokenSecret : String = "test-access-secret",
    refreshTokenSecret : String = "test-refresh-secret"
): JwtConfigs {
    return JwtConfigs(
        accessTokenExpiry = accessTokenExpiry,
        accessTokenSecretKey = accessTokenSecret,
        refreshTokenExpiry = refreshTokenExpiry,
        refreshTokenSecretKey = refreshTokenSecret,
        issuer = issuer,
    )
}

fun createAccessToken(member: Member, configs: JwtConfigs, timestamp: Long = 360000, permanent: Boolean = false): String {
    return JWT.create()
        .withIssuer(configs.issuer)
        .withClaim("id", member.id.toString())
        .withClaim("role", member.role)
        .withExpiresAt(Date(System.currentTimeMillis() + timestamp))
        .sign(Algorithm.HMAC512(configs.accessTokenSecretKey))
}

fun createRefreshToken(member: Member, configs: JwtConfigs, timestamp: Long = 360000, permanent: Boolean = false): String {
    return JWT.create()
        .withIssuer(configs.issuer)
        .withClaim("id", member.id.toString())
        .withClaim("role", member.role)
        .withExpiresAt(Date(System.currentTimeMillis() + timestamp))
        .sign(Algorithm.HMAC512(configs.refreshTokenSecretKey))
}