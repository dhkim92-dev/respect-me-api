package kr.respectme.group.port.`in`.interfaces.dto

import jakarta.persistence.Entity
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.GroupMemberRole
import java.time.Instant
import java.util.UUID

data class GroupMemberCommandResponse(
    val id: UUID,
    val nickname: String,
    val profileImage: String?,
    val createdAt: Instant,
    val role: GroupMemberRole
){

    companion object {
        fun valueOf(entity: GroupMember): GroupMemberCommandResponse {
            return GroupMemberCommandResponse(
                id = entity.id,
                nickname = entity.getNickname(),
                profileImage = null,
                createdAt = entity.getCreatedAt(),
                role = entity.getMemberRole()
            )
        }
    }
}