package kr.respectme.auth.application.dto

data class AuthenticationResult(
    val type: String,
    val accessToken: String,
    val refreshToken: String
)
