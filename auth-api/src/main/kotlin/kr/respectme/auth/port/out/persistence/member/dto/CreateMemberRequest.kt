package kr.respectme.auth.port.out.persistence.member.dto

data class CreateMemberRequest(
    val email: String,
    val profileImageUrl: String?,
) {

}