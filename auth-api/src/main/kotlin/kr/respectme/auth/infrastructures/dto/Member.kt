package kr.respectme.auth.infrastructures.dto

import java.util.UUID

data class Member(
    val id: UUID,
    val nickname: String,
    val email: String,
    val role: String,
    val isBlocked: Boolean,
    val blockReason: String,
) {

}