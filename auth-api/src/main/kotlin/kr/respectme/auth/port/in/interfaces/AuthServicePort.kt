package kr.respectme.auth.port.`in`.interfaces

import kr.respectme.auth.application.dto.JwtAccessTokenVerifierRequiredInfo
import kr.respectme.auth.port.`in`.interfaces.dto.LoginRequest
import kr.respectme.auth.port.`in`.interfaces.dto.LoginResponse
import kr.respectme.auth.port.`in`.interfaces.dto.RefreshAccessTokenRequest

interface AuthServicePort {

    fun login(request: LoginRequest): LoginResponse

    fun retrieveAccessTokenVerificationRequirements(jwtToken: String): JwtAccessTokenVerifierRequiredInfo

    fun reissueAccessToken(request: RefreshAccessTokenRequest): LoginResponse
}