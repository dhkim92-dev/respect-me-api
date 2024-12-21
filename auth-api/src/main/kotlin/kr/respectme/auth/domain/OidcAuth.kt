package kr.respectme.auth.domain

import jakarta.persistence.Convert
import jakarta.persistence.Embeddable

@Embeddable
data class OidcAuth(
    @Convert(converter = OidcPlatformConverter::class)
    val platform: OidcPlatform = OidcPlatform.NONE,
    val userIdentifier: String? = null,
) {

}