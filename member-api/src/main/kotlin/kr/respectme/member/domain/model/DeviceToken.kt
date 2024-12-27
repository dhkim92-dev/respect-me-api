package kr.respectme.member.domain.model

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.utility.UUIDV7Generator
import java.time.Instant
import java.util.UUID

@DomainEntity
class DeviceToken(
    id: UUID = UUIDV7Generator.generate(),
    private val memberId: UUID = UUIDV7Generator.generate(),
    private val type: DeviceTokenType = DeviceTokenType.FCM,
    private val createdAt: Instant = Instant.now(),
    private val token: String = "",
    private var lastUsedAt: Instant = Instant.now(),
    private var isActivated: Boolean = true,
    private var isDeleted: Boolean = false
): BaseDomainEntity<UUID>(id) {

    fun getMemberId(): UUID {
        return memberId
    }

    fun getType(): DeviceTokenType {
        return type
    }

    fun getCreatedAt(): Instant {
        return createdAt
    }

    fun getToken(): String {
        return token
    }

    fun getLastUsedAt(): Instant {
        return lastUsedAt
    }

    fun getIsActivated(): Boolean {
        return isActivated
    }

    fun getIsDeleted(): Boolean {
        return isDeleted
    }

    fun revokeDeviceToken() {
        isDeleted = true
        isActivated = false
    }

    fun softDelete() {
        isDeleted = true
    }

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