package kr.respectme.group.application.dto.group

import kr.respectme.group.domain.GroupType
import kr.respectme.group.domain.NotificationGroup
import java.time.Instant
import java.util.UUID

data class NotificationGroupDto(
    val id: UUID=UUID.randomUUID(),
    val name: String="",
    val ownerId: UUID=UUID.randomUUID(),
    val description: String="",
    val createdAt: Instant=Instant.now(),
    val imageUrl: String?=null,
    val groupType: GroupType=GroupType.GROUP_PRIVATE,
    val memberCount: Int = 0,
) {

    companion object {
        fun valueOf(group: NotificationGroup): NotificationGroupDto {
            return NotificationGroupDto(
                id = group.id,
                name = group.name,
                ownerId = group.ownerId,
                description = group.description,
                imageUrl = "",
                groupType = group.type,
                createdAt = group.createdAt,
                memberCount = group.members.size
            )
        }
    }
}