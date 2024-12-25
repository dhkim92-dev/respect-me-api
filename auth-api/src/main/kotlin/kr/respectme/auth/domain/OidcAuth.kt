package kr.respectme.auth.domain

import jakarta.persistence.Convert
import jakarta.persistence.Embeddable

@Embeddable
data class OidcAuth(
    @Convert(converter = OidcPlatformConverter::class)
    val platform: OidcPlatform = OidcPlatform.NONE,
    val userIdentifier: String? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OidcAuth
        return (other.platform == platform) && (other.userIdentifier == userIdentifier)
    }

    override fun toString(): String {
        return """{
            platform=$platform, 
            userIdentifier='$userIdentifier'
            }""".trimMargin()
    }
}