package kr.respectme.auth.application.useCase

import kr.respectme.auth.application.dto.AuthenticationResult
import kr.respectme.auth.application.dto.JwtAccessTokenVerifierRequiredInfo
import kr.respectme.auth.port.`in`.interfaces.dto.LoginRequest

interface AuthUseCase {

    @Deprecated("Use loginWithOidc instead")
    fun login(loginRequest: LoginRequest): AuthenticationResult

    fun retrieveAccessTokenVerifierRequiredInfo(accessToken: String): JwtAccessTokenVerifierRequiredInfo

    fun refreshAccessToken(refreshToken: String): AuthenticationResult
}