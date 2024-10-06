package kr.respectme.member.domain.model

import kr.respectme.common.utility.UUIDV7Generator
import java.time.Instant
import java.util.UUID

class DeviceToken(
    val id: UUID = UUIDV7Generator.generate(),
    val memberId: UUID = UUIDV7Generator.generate(),
    val type: DeviceTokenType = DeviceTokenType.FCM,
    val createdAt: Instant = Instant.now(),
    token: String = "",
    lastUsedAt: Instant = Instant.now(),
    isActivated: Boolean = true
) {

    var token: String = token
        private set

    var lastUsedAt = lastUsedAt
        private set

    var isActivated = isActivated
        private set

    fun useToken() {
        lastUsedAt = Instant.now()
    }

    fun isTokenActivated(): Boolean {
        return isActivated
    }

    fun deactivateToken() {
        isActivated = false
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DeviceToken

        if (id != other.id) return false

        return true
    }
}