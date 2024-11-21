package kr.respectme.message.infrastructure.msa.dto

import java.time.Instant
import java.util.*

class Member(
    val id: UUID,
    val nickname: String,
    val email: String,
    val role: String,
    val createdAt: Instant,
    val blockReason: String,
    val deviceTokens: List<String>,
    val blocked: Boolean,
) {

}