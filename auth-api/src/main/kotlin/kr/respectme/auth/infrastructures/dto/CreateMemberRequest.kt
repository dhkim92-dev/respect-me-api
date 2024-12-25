package kr.respectme.auth.infrastructures.dto

data class CreateMemberRequest(
    val email: String,
    val nickname: String? = null,
    val profileImageUrl: String?,
) {

}