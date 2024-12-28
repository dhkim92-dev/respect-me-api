package kr.respectme.auth.port.`in`.msa.members.dto

import java.util.UUID

data class Member(
    val id: UUID,
    val email: String,
    val role: String,
    val isBlocked: Boolean,
    val blockReason: String,
) {

}