package kr.respectme.member.domain.dto

import kr.respectme.member.domain.model.MemberRole
import java.time.LocalDateTime
import java.util.UUID

class MemberDto(
    val id: UUID,
    val email: String,
    val nickname: String,
    val role: MemberRole,
    val createdAt: LocalDateTime,
    val isBlocked: Boolean,
    val blockReason: String
) {

}