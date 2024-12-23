package kr.respectme.auth.domain

import jakarta.persistence.Embeddable

enum class OidcPlatform(val value: Int) {
    NONE(0),
    APPLE(1),
    GOOGLE(2),
}