package kr.respectme.member.applications.dto

import kr.respectme.member.domain.model.DeviceToken
import kr.respectme.member.domain.model.DeviceTokenType
import kr.respectme.member.infrastructures.persistence.jpa.JpaDeviceTokenEntity
import java.time.Instant
import java.util.UUID

data class DeviceTokenDto(
    val id: UUID,
    val memberId: UUID,
    val type: DeviceTokenType,
    val token: String,
    val createdAt: Instant,
    val lastUsedAt: Instant
) {

    companion object {

        fun valueOf(entity: DeviceToken): DeviceTokenDto {
            return DeviceTokenDto(
                id = entity.id,
                type = entity.type,
                memberId = entity.memberId,
                token = entity.token,
                createdAt = entity.createdAt,
                lastUsedAt = entity.lastUsedAt
            )
        }

        fun valueOf(entity: JpaDeviceTokenEntity): DeviceTokenDto {
            return DeviceTokenDto(
                id = entity.id,
                memberId = entity.member.id,
                type = entity.type,
                token = entity.token,
                createdAt = entity.createdAt,
                lastUsedAt = entity.lastUsedAt
            )
        }
    }
}