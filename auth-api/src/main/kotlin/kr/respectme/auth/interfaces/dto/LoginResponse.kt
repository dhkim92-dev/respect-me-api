package kr.respectme.auth.interfaces.dto

import kr.respectme.auth.application.dto.AuthenticationResult
import java.util.*

data class LoginResponse(
    val type: String,
    val memberId: UUID,
    val accessToken: String,
    val refreshToken: String
){

    companion object {
        fun of(result: AuthenticationResult): LoginResponse {
            return LoginResponse(
                type = result.type,
                memberId = result.memberId,
                accessToken = result.accessToken,
                refreshToken = result.refreshToken
            )
        }
    }
}