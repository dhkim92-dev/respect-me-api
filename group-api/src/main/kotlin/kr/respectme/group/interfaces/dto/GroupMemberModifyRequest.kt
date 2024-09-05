package kr.respectme.group.interfaces.dto

import jakarta.validation.constraints.NotBlank
import java.util.UUID

data class GroupMemberModifyRequest(
    @field: NotBlank(message = "nickname must not be null or empty.")
    val nickname: String? = null
){

}