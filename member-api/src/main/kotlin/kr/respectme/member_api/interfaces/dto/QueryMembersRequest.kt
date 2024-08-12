package kr.respectme.member_api.interfaces.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotEmpty
import java.util.UUID

data class QueryMembersRequest(
    @field: NotEmpty(message = "memberIds field should not me empty.")
    val memberIds: List<UUID>
)
