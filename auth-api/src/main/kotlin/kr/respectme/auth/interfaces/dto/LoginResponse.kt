package kr.respectme.auth.interfaces.dto

import kr.respectme.auth.application.dto.AuthenticationResult

data class LoginResponse(
    val type: String,
    val accessToken: String,
    val refreshToken: String
){

    companion object {
        fun of(result: AuthenticationResult): LoginResponse {
            return LoginResponse(
                type = result.type,
                accessToken = result.accessToken,
                refreshToken = result.refreshToken
            )
        }
    }
}