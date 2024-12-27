package kr.respectme.member.applications.dto

import kr.respectme.member.port.`in`.dto.LoginRequest

data class LoginCommand(
    val email: String,
    val password: String
){

    companion object {
        fun of(request: LoginRequest): LoginCommand {
            return LoginCommand(
                email = request.email,
                password = request.password
            )
        }
    }
}