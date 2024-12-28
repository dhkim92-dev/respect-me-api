package kr.respectme.auth.port.`in`.interfaces

import kr.respectme.auth.port.`in`.interfaces.dto.LoginResponse
import kr.respectme.auth.port.`in`.interfaces.dto.OidcLoginRequest

interface OidcAuthPort {

    fun loginWithApple(request: OidcLoginRequest): LoginResponse

    fun loginWithGoogle(request: OidcLoginRequest): LoginResponse
}