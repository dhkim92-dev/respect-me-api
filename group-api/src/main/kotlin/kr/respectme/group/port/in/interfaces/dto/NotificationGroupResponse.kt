package kr.respectme.group.port.`in`.interfaces.dto

import kr.respectme.group.application.dto.group.NotificationGroupDto
import java.util.*

data class NotificationGroupResponse(
    val groupId: UUID,
    val groupName: String,
    val ownerId: UUID,
    val groupDescription: String,
    val groupImageUrl: String? = null,
    val groupType: String,
) {
    companion object {

        fun valueOf(
            groupDto: NotificationGroupDto
        ): NotificationGroupResponse {
            return NotificationGroupResponse(
                groupId = groupDto.id,
                groupName = groupDto.name,
                groupDescription = groupDto.description,
                groupImageUrl = groupDto.imageUrl,
                groupType = groupDto.groupType.name,
                ownerId = groupDto.ownerId
            )
        }
    }
}