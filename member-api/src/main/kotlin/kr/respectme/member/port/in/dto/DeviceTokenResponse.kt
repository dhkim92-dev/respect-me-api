package kr.respectme.member.port.`in`.dto

import kr.respectme.member.applications.dto.DeviceTokenDto
import kr.respectme.member.domain.model.DeviceTokenType
import java.time.Instant
import java.util.UUID

data class DeviceTokenResponse(
    val id: UUID,
    val type: DeviceTokenType,
    val token: String,
    val createdAt: Instant,
    val lastUsedAt: Instant
) {
    companion object {
        fun of(dto: DeviceTokenDto): DeviceTokenResponse {
            return DeviceTokenResponse(
                id = dto.id,
                type = dto.type,
                token = dto.token,
                createdAt = dto.createdAt,
                lastUsedAt = dto.lastUsedAt
            )
        }
    }
}
