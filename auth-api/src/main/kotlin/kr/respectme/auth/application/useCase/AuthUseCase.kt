package kr.respectme.auth.application.useCase

import kr.respectme.auth.application.dto.AuthenticationResult
import kr.respectme.auth.application.dto.JwtAccessTokenVerifierRequiredInfo
import kr.respectme.auth.application.dto.TokenValidationResult
import kr.respectme.auth.infrastructures.dto.LoginRequest
import java.util.*

interface AuthUseCase {

    @Deprecated("Use loginWithOidc instead")
    fun login(loginRequest: LoginRequest): AuthenticationResult

    fun retrieveAccessTokenVerifierRequiredInfo(accessToken: String): JwtAccessTokenVerifierRequiredInfo

    fun refreshAccessToken(refreshToken: String): AuthenticationResult
}