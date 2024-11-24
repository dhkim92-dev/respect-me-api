package kr.respectme.group.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.application.dto.group.NotificationGroupDto
import java.util.UUID

@Schema(description = "그룹 정보")
data class NotificationGroupVo(
    @Schema(description = "그룹 ID")
    val id: UUID,
    @Schema(description = "그룹 이름")
    val name: String,
    @Schema(description = "그룹 소유자 ID")
    val ownerId: UUID,
    @Schema(description = "그룹 설명")
    val description: String,
    @Schema(description = "그룹 이미지 URL")
    val imageUrl: String?,
    @Schema(description = "그룹 타입", example = "GROUP_PUBLIC")
    val groupType: String,
    @Schema(description = "그룹 멤버 수")
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