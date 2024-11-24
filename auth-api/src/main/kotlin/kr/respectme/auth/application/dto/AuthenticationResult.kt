package kr.respectme.auth.application.dto

import java.util.UUID

data class AuthenticationResult(
    val type: String,
    val memberId: UUID,
    val accessToken: String,
    val refreshToken: String
)
