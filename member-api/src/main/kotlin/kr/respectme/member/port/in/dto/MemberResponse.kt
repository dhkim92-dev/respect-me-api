package kr.respectme.member.port.`in`.dto

import kr.respectme.member.domain.dto.MemberDto
import kr.respectme.member.domain.model.MemberRole
import java.time.Instant
import java.util.UUID

data class MemberResponse(
    val id: UUID,
    val email: String,
    val role: MemberRole,
    val isBlocked: Boolean,
    val createdAt: Instant,
    val blockReason: String,
    val deviceTokens: List<String> = emptyList()
) {

    companion object {
        fun of(memberDto: MemberDto): MemberResponse {
            return MemberResponse(
                id = memberDto.id,
                email = memberDto.email,
                role = memberDto.role,
                isBlocked = memberDto.isBlocked,
                createdAt = memberDto.createdAt,
                blockReason = memberDto.blockReason,
                deviceTokens = memberDto.deviceTokens
            )
        }
    }
}