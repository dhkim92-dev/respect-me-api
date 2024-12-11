package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "그룹 멤버 생성 요청 객체")
data class GroupMemberCreateRequest(
    @Schema(required = true, example = "nickname", description = "그룹 내에서 사용할 닉네임")
    @field: NotBlank(message = "nickname must not be null or empty.")
    val nickname: String,
    @Schema(required = true, example = "password", description = "가입하려는 그룹 비밀번호가 지정 되었을 시, 그룹 비밀번호")
    val groupPassword: String?
) {

}