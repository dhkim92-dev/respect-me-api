package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.domain.GroupMemberRole
import java.time.Instant
import java.util.UUID

@Schema(description = "그룹 멤버 정보")
data class GroupMemberVo(
    @Schema(description = "멤버 ID")
    val id : UUID,
    @Schema(description = "닉네임")
    val nickname: String,
    @Schema(description = "프로필 이미지 URL")
    val profileImage: String?,
    @Schema(description = "생성일시")
    val createdAt: Instant,
    @Schema(description = "역할", example = "MEMBER")
    val role: GroupMemberRole
) {

    companion object {
        fun valueOf(memberDto: GroupMemberDto): GroupMemberVo {
            return GroupMemberVo(
                id = memberDto.memberId,
                nickname = memberDto.nickname,
                createdAt = memberDto.createdAt,
                profileImage = memberDto.profileImageUrl,
                role = memberDto.role
            )
        }
    }
}