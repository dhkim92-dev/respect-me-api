package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.application.dto.group.GroupQueryModelDto
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.domain.GroupType
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import java.util.UUID

@Schema(description = "그룹 정보")
data class NotificationGroupQueryResponse(
    @Schema(description = "그룹 ID")
    val id: UUID,
    @Schema(description = "그룹 이름")
    val name: String,
    @Schema(description = "그룹 소유자 정보")
    val groupOwner: GroupMemberVo = GroupMemberVo(),
    @Schema(description = "그룹 설명")
    val description: String,
    @Schema(description = "그룹 이미지 URL")
    val thumbnail: String?,
    @Schema(description = "그룹 타입", example = "GROUP_PUBLIC")
    val groupType: GroupType = GroupType.GROUP_PUBLIC,
    @Schema(description = "그룹 멤버 수")
    val memberCount: Int,
    @Schema(description = "그룹에서의 내 역할, OWNER, ADMIN, MEMBER, GUEST 로 구분, GUEST는 비회원", example = "GUEST")
    val myRole: GroupMemberRole = GroupMemberRole.MEMBER
) {

    companion object {

        fun valueOf(dto: GroupQueryModelDto): NotificationGroupQueryResponse {
            return NotificationGroupQueryResponse(
                id = dto.id,
                name = dto.name,
                groupOwner = dto.ownerInfo,
                description = dto.description,
                thumbnail = dto.thumbnail,
                groupType = dto.type,
                memberCount = dto.memberCount,
                myRole = dto.myRole
            )
        }
    }
}