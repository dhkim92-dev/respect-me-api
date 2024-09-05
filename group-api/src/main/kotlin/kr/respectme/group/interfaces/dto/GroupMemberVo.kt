package kr.respectme.group.interfaces.dto

import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.domain.GroupMemberRole
import java.time.Instant
import java.util.UUID

data class GroupMemberVo(
    val id : UUID,
    val nickname: String,
    val profileImage: String?,
    val createdAt: Instant,
    val role: GroupMemberRole
) {

    companion object {
        fun valueOf(memberDto: GroupMemberDto): GroupMemberVo {
            return GroupMemberVo(
                id = memberDto.memberId,
                nickname = memberDto.nickname,
                createdAt = memberDto.createdAt,
                profileImage = memberDto.profileImageUrl,
                role = memberDto.role
            )
        }
    }
}