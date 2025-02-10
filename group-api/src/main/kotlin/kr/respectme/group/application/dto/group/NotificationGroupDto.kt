package kr.respectme.group.application.dto.group

import kr.respectme.group.domain.GroupType
import kr.respectme.group.domain.NotificationGroup
import java.time.Instant
import java.util.UUID

data class NotificationGroupDto(
    val id: UUID=UUID.randomUUID(),
    val name: String="",
    val ownerId: UUID = UUID.randomUUID(),
    val description: String="",
    val createdAt: Instant=Instant.now(),
    val thumbnail: Long?=null,
    val groupType: GroupType=GroupType.GROUP_PRIVATE
) {

    companion object {
        fun valueOf(group: NotificationGroup): NotificationGroupDto {
            return NotificationGroupDto(
                id = group.id,
                name = group.getName(),
                ownerId = group.getOwnerId(),
                description = group.getDescription(),
                thumbnail = group.getThumbnail(),
                groupType = group.getType(),
                createdAt = group.getCreatedAt(),
            )
        }
    }
}