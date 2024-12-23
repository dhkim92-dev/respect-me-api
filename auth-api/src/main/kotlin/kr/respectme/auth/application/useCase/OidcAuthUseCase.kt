package kr.respectme.auth.application.useCase

import kr.respectme.auth.application.dto.AuthenticationResult
import kr.respectme.auth.application.dto.OidcMemberLoginCommand
import kr.respectme.auth.application.dto.OidcMemberLogoutCommand
import kr.respectme.auth.domain.OidcPlatform

interface OidcAuthUseCase {

    fun loginWithOidc(command: OidcMemberLoginCommand): AuthenticationResult

//    fun logout(command: OidcMemberLogoutCommand): Boolean
}