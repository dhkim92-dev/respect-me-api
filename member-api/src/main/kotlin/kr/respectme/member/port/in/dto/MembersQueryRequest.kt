package kr.respectme.member.port.`in`.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

@Schema(description = "회원 조회 다건 요청 객체")
data class MembersQueryRequest(
    val memberIds: List<UUID> = emptyList()
)
