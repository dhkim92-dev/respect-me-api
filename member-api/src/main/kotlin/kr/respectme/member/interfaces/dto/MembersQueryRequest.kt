package kr.respectme.member.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import java.util.UUID

@Schema(description = "회원 조회 다건 요청 객체")
data class MembersQueryRequest(
    @field: NotEmpty(message = "memberIds field should not me empty.")
    val memberIds: List<UUID>
)
