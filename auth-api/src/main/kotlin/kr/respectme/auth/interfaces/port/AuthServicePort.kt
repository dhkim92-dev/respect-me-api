package kr.respectme.auth.interfaces.port

import com.google.auth.oauth2.AccessToken
import kr.respectme.auth.application.dto.JwtAccessTokenVerifierRequiredInfo
import kr.respectme.auth.infrastructures.dto.LoginRequest
import kr.respectme.auth.interfaces.dto.LoginResponse
import kr.respectme.auth.interfaces.dto.RefreshAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenResponse

interface AuthServicePort {

    fun login(request: LoginRequest): LoginResponse

    fun retrieveAccessTokenVerificationRequirements(jwtToken: String): JwtAccessTokenVerifierRequiredInfo

    fun reissueAccessToken(request: RefreshAccessTokenRequest): LoginResponse
}