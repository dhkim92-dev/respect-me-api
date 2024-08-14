package kr.respectme.common.security.jwt.adapter.dto

data class JwtValidateRequest(
    val type: String,
    val accessToken: String
) {

}