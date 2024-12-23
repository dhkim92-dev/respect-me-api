package kr.respectme.auth.application.dto

import kr.respectme.auth.domain.OidcPlatform

class OidcMemberLogoutCommand(
    val platform: OidcPlatform,
    val idToken: String
) {

}