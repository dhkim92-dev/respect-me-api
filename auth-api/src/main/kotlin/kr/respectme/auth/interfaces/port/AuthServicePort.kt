package kr.respectme.auth.interfaces.port

import kr.respectme.auth.infrastructures.dto.LoginRequest
import kr.respectme.auth.interfaces.dto.LoginResponse
import kr.respectme.auth.interfaces.dto.RefreshAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenResponse

interface AuthServicePort {

    fun login(request: LoginRequest): LoginResponse

    fun verifyAccessToken(request: VerifyAccessTokenRequest): VerifyAccessTokenResponse

    fun reissueAccessToken(request: RefreshAccessTokenRequest): LoginResponse
}