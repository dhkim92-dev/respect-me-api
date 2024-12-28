package kr.respectme.auth.port.`in`.interfaces.dto

import jakarta.validation.constraints.NotBlank

data class VerifyAccessTokenRequest(
    @field: NotBlank(message="type must not be null or empty.")
    val type: String,
    @field: NotBlank(message="access token must not be null or empty.")
    val accessToken: String
) {

}