package kr.respectme.auth.port.out.persistence.member.dto

import java.util.UUID

data class Member(
    val id: UUID,
    val email: String,
    val role: String,
    val isBlocked: Boolean,
    val blockReason: String,
) {

}