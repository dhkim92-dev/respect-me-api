package kr.respectme.auth.interfaces.dto

import jakarta.validation.constraints.NotBlank

data class RefreshAccessTokenRequest(
    @field:NotBlank(message = "type must not be null or empty")
    val type: String,
    @field:NotBlank(message = "refresh token must not be null or empty")
    val refreshToken: String
) {

}