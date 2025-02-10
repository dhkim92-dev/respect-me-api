package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import kr.respectme.group.domain.GroupType
import org.hibernate.validator.constraints.Length

@Schema(description = "그룹 생성 요청 객체")
data class GroupCreateRequest(
    @Schema(required = true, example = "Group Name", description = "그룹 이름")
    @field: NotBlank(message = "Group name must not be null or empty.")
    @field: Length(max=12, message = "Group name must be less than 12 characters.")
    val name: String,

    @Schema(required = true, example = "Group Description", description = "그룹 설명")
    @field: NotBlank(message = "Group description must not be null or empty.")
    @field: Length(max=500, message = "Group description must be less than 500 characters.")
    val description: String = "",

    @Schema(required = true, example = "PRIVATE", description = "그룹 타입")
//    @field: NotBlank(message = "Group type must not be null or empty.")
    val type: GroupType = GroupType.GROUP_PRIVATE,

    @Schema(required = true, description = "썸네일 이미지 파일 ID, File API를 통해 업로드 후 받은 이미지 파일 id를 입력 해야 합니다.", example = "1")
    val thumbnail: Long? = null,

    @Schema(required=true, description = "그룹 소유자 닉네임")
    @field: NotBlank(message = "Group owner nickname must not be null or empty.")
    @field: Length(max=12, message = "Group owner nickname must be less than 12 characters.")
    val ownerNickname: String
) {
}