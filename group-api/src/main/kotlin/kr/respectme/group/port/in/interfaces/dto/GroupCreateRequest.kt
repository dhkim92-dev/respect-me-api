package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import kr.respectme.group.domain.GroupType

@Schema(description = "그룹 생성 요청 객체")
data class GroupCreateRequest(
    @Schema(required = true, example = "Group Name", description = "그룹 이름")
    @field: NotBlank(message = "Group name must not be null or empty.")
    val name: String,
    @Schema(required = true, example = "Group Description", description = "그룹 설명")
    @field: NotBlank(message = "Group description must not be null or empty.")
    val description: String = "",
    @Schema(required = true, example = "PRIVATE", description = "그룹 타입")
    @field: NotBlank(message = "Group type must not be null or empty.")
    val type: GroupType = GroupType.GROUP_PRIVATE,
) {
}