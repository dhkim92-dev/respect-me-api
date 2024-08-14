package kr.respectme.auth.application.port

import kr.respectme.auth.application.dto.AuthenticationResult
import kr.respectme.auth.application.dto.TokenValidationResult
import kr.respectme.auth.infrastructures.dto.LoginRequest

interface AuthenticationUseCase {

    fun login(loginRequest: LoginRequest): AuthenticationResult

    fun validateToken(token: String): TokenValidationResult

    fun refreshAccessToken(refreshToken: String): AuthenticationResult
}