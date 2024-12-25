package kr.respectme.auth.application.dto

import kr.respectme.auth.domain.OidcPlatform

data class OidcMemberLoginCommand(
    val platform: OidcPlatform,
    val idToken: String
) {

}