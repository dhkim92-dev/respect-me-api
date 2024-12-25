package kr.respectme.auth.support

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kr.respectme.auth.application.oidc.idToken.CommonOidcIdTokenPayload
import kr.respectme.auth.configs.JwtConfigs
import kr.respectme.auth.domain.MemberAuthInfo
import kr.respectme.auth.domain.OidcPlatform
import kr.respectme.auth.infrastructures.dto.Member
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.util.*

fun createJwtConfigs(
    accessTokenExpiry : Long = 0,
    refreshTokenExpiry : Long = 0,
    issuer : String = "https://identification.respect-me.kr",
    accessTokenSecret : String = "local-test-access-secret",
    refreshTokenSecret : String = "local-test-refresh-secret"
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
        .withArrayClaim("roles", arrayOf(member.role))
        .withClaim("email", member.email)
        .withClaim("nickname", member.nickname)
        .withClaim("isActivated", !member.isBlocked)
        .withSubject(member.id.toString())
        .withExpiresAt(Date(System.currentTimeMillis() + timestamp))
        .sign(Algorithm.HMAC256(configs.accessTokenSecretKey))
}

fun createRefreshToken(member: Member, configs: JwtConfigs, timestamp: Long = 360000, permanent: Boolean = false): String {
    return JWT.create()
        .withIssuer(configs.issuer)
        .withSubject(member.id.toString())
        .withExpiresAt(Date(System.currentTimeMillis() + timestamp))
        .sign(Algorithm.HMAC256(configs.refreshTokenSecretKey))
}

fun createServiceAccountToken(configs: JwtConfigs): String {
    return JWT.create()
        .withIssuer(configs.issuer)
        .withClaim("id", UUID.randomUUID().toString())
        .withClaim("email", "service-account@respect-me.kr")
        .withClaim("nickname", "service")
        .withClaim("isActivated", true)
        .withSubject(UUID.randomUUID().toString())
        .withArrayClaim("roles", arrayOf("ROLE_SERVICE"))
        .withExpiresAt(Date(System.currentTimeMillis() + 360000))
        .sign(Algorithm.HMAC256(configs.accessTokenSecretKey))
}

fun createRSAKeyPair(): KeyPair {
    val keyGen = KeyPairGenerator.getInstance("RSA")
    keyGen.initialize(2048)
    return keyGen.generateKeyPair()
}

fun createSampleAppleIdToken(
    keyPair: KeyPair,
    issuer: String = "https://appleid.apple.com",
    audience: String = "sample-audience",
    keyId: String = "sample-kid",
    sub: String = "sample-sub",
    email: String = "sample-email@respect-me.kr",
    emailVerified: Boolean = true,
    isPrivateEmail: Boolean = true,
    nonceSupported: Boolean = true
): String {
    val algorithm = Algorithm.RSA256(null, keyPair.private as RSAPrivateKey)
    return JWT.create()
        .withIssuer(issuer)
        .withKeyId(keyId)
        .withSubject(sub)
        .withAudience(audience)
        .withClaim("email", email)
        .withClaim("email_verified", emailVerified)
        .withClaim("is_private_email", isPrivateEmail)
        .withClaim("nonce_supported", nonceSupported)
        .withIssuedAt(Date(System.currentTimeMillis()))
        .withExpiresAt(Date(System.currentTimeMillis() + 360000))
        .sign(algorithm)
}

fun createSampleGoogleIdToken(
    keyPair: KeyPair,
    keyId: String = "sample-kid",
    issuer: String = "https://accounts.google.com",
    audience: String = "sample-audience",
    sub: String = "sample-sub",
    email: String = "sample-email@respect-me.kr",
    emailVerified: Boolean = true,
    name: String = "sample-name",
    picture: String = "https://sample.com/sample.jpg",
    givenName: String = "sample-given-name",
    familyName: String = "sample-family-name",
    locale: String = "ko-KR",
): String {
    val algorithm = Algorithm.RSA256(null, keyPair.private as RSAPrivateKey)
    return JWT.create()
        .withIssuer(issuer)
        .withKeyId(keyId)
        .withSubject(sub)
        .withAudience(audience)
        .withClaim("email", email)
        .withClaim("email_verified", emailVerified)
        .withClaim("name", name)
        .withClaim("picture", picture)
        .withClaim("given_name", givenName)
        .withClaim("family_name", familyName)
        .withClaim("locale", locale)
        .withIssuedAt(Date(System.currentTimeMillis()))
        .withExpiresAt(Date(System.currentTimeMillis() + 360000))
        .sign(algorithm)
}

fun createOidcCommonPayload(
    idToken: String
): CommonOidcIdTokenPayload {
    val decodedToken = JWT.decode(idToken)

    return CommonOidcIdTokenPayload(
        iss = decodedToken.issuer,
        sub = decodedToken.subject,
        aud = decodedToken.audience,
        exp = decodedToken.expiresAt.time,
        iat = decodedToken.issuedAt.time,
        email = decodedToken.getClaim("email").asString(),
        name = decodedToken.getClaim("name").asString(),
        profileImageUrl = ""
    )
}