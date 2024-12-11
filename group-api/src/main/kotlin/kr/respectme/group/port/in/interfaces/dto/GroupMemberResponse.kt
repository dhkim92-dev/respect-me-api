package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.domain.GroupMemberRole
import java.util.*

@Schema(description = "그룹 멤버 응답 객체")
data class GroupMemberResponse(
    @Schema(description = "그룹 ID")
    val groupId: UUID,
    @Schema(description = "멤버 ID")
    val memberId: UUID,
    @Schema(description = "닉네임")
    val nickname: String,
    @Schema(description = "역할", example = "MEMBER")
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