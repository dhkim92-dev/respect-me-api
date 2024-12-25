package kr.respectme.auth.infrastructures.dto

data class CreateMemberRequest(
    val email: String,
    val profileImageUrl: String?,
) {

}