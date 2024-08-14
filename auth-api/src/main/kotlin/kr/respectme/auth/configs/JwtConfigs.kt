package kr.respectme.auth.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfigs(
    @Value("\${jwt.access-token.secret-key}")
    val accessTokenSecretKey: String,
    @Value("\${jwt.refresh-token.secret-key}")
    val refreshTokenSecretKey: String,
    @Value("\${jwt.access-token.expires-in}")
    val accessTokenExpiry: Long,
    @Value("\${jwt.refresh-token.expires-in}")
    val refreshTokenExpiry: Long,
    @Value("\${jwt.issuer}")
    val issuer: String,
) {

}