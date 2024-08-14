package kr.respectme.auth.application.dto

import java.time.LocalDateTime
import java.util.*

data class TokenValidationResult(
    val type: String,
    val issuer: String,
    val expiresAt: LocalDateTime,
    val memberId: UUID,
    val roles: Array<String>,
    val email: String,
    val nickname: String,
    val isActivated: Boolean
){
}