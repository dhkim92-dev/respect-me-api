package kr.respectme.member.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "회원 정보 수정 요청 객체")
data class ModifyMemberRequest(
    @Schema(description = "닉네임", example = "Lion King", minLength = 4, maxLength = 64)
    val nickname: String?,
    @Schema(description = "현재 비밀번호", example = "password")
    val password: String?,
    @Schema(description = "새로운 비밀번호", example = "newPassword")
    val newPassword: String?
) {

}