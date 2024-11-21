package kr.respectme.message.infrastructure.msa.dto

import java.util.UUID

class MemberDto(
    val id: UUID,
    val deviceTokens: List<UUID>,
) {
}