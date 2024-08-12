package kr.respectme.member_api.interfaces.dto

import jakarta.validation.constraints.NotBlank

data class ModifyMemberRequest(
    val nickname: String?,
    val password: String?,
    val newPassword: String?
) {

}