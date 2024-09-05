package kr.respectme.group.interfaces.dto

import kr.respectme.group.application.dto.group.NotificationGroupDto
import java.util.UUID

data class NotificationGroupVo(
    val id: UUID,
    val name: String,
    val ownerId: UUID,
    val description: String,
    val imageUrl: String?,
    val groupType: String,
    val memberCount: Int,
) {

    companion object {

        fun valueOf(group: NotificationGroupDto): NotificationGroupVo {
            return NotificationGroupVo(
                id = group.id,
                name = group.name,
                ownerId = group.ownerId,
                description = group.description,
                imageUrl = group.imageUrl,
                groupType = group.groupType.name,
                memberCount = group.memberCount
            )
        }
    }
}