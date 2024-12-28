package kr.respectme.auth.port.`in`.msa.members.dto

data class CreateMemberRequest(
    val email: String,
    val profileImageUrl: String?,
) {

}