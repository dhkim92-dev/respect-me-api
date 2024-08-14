package kr.respectme.member.interfaces.dto

data class ModifyMemberRequest(
    val nickname: String?,
    val password: String?,
    val newPassword: String?
) {

}