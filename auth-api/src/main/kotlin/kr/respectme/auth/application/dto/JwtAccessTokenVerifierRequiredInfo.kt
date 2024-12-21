package kr.respectme.auth.application.dto

data class JwtAccessTokenVerifierRequiredInfo(
    val issuer: String,
    val secret: String
) {
    
}