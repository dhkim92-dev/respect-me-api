package kr.respectme.group.application.dto.member

import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.GroupMemberRole
import java.time.Instant
import java.util.UUID

class GroupMemberDto(
    val groupId: UUID,
    val memberId: UUID,
    val nickname: String,
    val profileImageUrl: String?,
    val createdAt: Instant,
    val role: GroupMemberRole
) {

    companion object {

        fun valueOf(entity: GroupMember): GroupMemberDto {
            return GroupMemberDto(
                groupId = entity.groupId,
                memberId = entity.memberId,
                nickname = entity.nickname,
                profileImageUrl = entity.profileImageUrl,
                role = entity.memberRole,
                createdAt = entity.createdAt
            )
        }
    }
}