package kr.respectme.common.security.jwt.adapter.dto

data class JwtAuthenticationRequirements(
    val issuer: String,
    val secret: String
) {
}