package kr.respectme.auth.interfaces.port

import kr.respectme.auth.interfaces.dto.LoginResponse
import kr.respectme.auth.interfaces.dto.OidcLoginRequest

interface OidcAuthPort {

    fun loginWithApple(request: OidcLoginRequest): LoginResponse

    fun loginWithGoogle(request: OidcLoginRequest): LoginResponse
}