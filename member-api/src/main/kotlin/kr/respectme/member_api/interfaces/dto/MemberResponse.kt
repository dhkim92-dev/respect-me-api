package kr.respectme.member_api.interfaces.dto

import kr.respectme.member_api.domain.dto.MemberDto
import kr.respectme.member_api.domain.model.MemberRole
import java.time.LocalDateTime
import java.util.UUID

data class MemberResponse(
    val id: UUID,
    val nickname: String,
    val email: String,
    val role: MemberRole,
    val isBlocked: Boolean,
    val createdAt: LocalDateTime,
    val blockReason: String
) {

    companion object {
        fun of(memberDto: MemberDto): MemberResponse {
            return MemberResponse(
                id = memberDto.id,
                nickname = memberDto.nickname,
                email = memberDto.email,
                role = memberDto.role,
                isBlocked = memberDto.isBlocked,
                createdAt = memberDto.createdAt,
                blockReason = memberDto.blockReason
            )
        }
    }
}