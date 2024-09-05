package kr.respectme.member.interfaces.dto

import kr.respectme.member.domain.dto.MemberDto
import kr.respectme.member.domain.model.MemberRole
import java.time.Instant
import java.util.UUID

data class MemberResponse(
    val id: UUID,
    val nickname: String,
    val email: String,
    val role: MemberRole,
    val isBlocked: Boolean,
    val createdAt: Instant,
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