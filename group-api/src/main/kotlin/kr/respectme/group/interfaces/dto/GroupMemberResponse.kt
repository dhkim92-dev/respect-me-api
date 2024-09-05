package kr.respectme.group.interfaces.dto

import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.domain.GroupMemberRole
import java.util.*

data class GroupMemberResponse(
    val groupId: UUID,
    val memberId: UUID,
    val nickname: String,
    val role: GroupMemberRole
) {

    companion object {
        fun of(dto: GroupMemberDto): GroupMemberResponse {
            return GroupMemberResponse(
                groupId = dto.groupId,
                memberId = dto.memberId,
                nickname = dto.nickname,
                role = dto.role
            )
        }
    }
}