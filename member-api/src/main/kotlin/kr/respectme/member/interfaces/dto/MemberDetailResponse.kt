package kr.respectme.member.interfaces.dto

import kr.respectme.member.domain.dto.MemberDto
import kr.respectme.member.domain.model.MemberRole
import java.util.UUID

data class MemberDetailResponse(
    val id : UUID,
    val email: String,
    val nickname: String,
    val role: MemberRole,
) {

    companion object {
        fun of(memberDto: MemberDto): MemberDetailResponse {
            return MemberDetailResponse(
                id = memberDto.id,
                email = memberDto.email,
                nickname = memberDto.nickname,
                role = memberDto.role
            )
        }
    }
}
