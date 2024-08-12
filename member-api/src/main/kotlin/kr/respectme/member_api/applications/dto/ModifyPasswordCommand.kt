package kr.respectme.member_api.applications.dto

import jakarta.validation.constraints.NotBlank
import kr.respectme.member_api.interfaces.dto.ModifyMemberRequest
import java.util.UUID

data class ModifyPasswordCommand(
    val resourceId: UUID,
    @field:NotBlank(message = "Password must not be null or empty.")
    val password: String,
    @field:NotBlank(message = "New password must not be null or empty.")
    val newPassword: String
){

    companion object {
        fun of(resourceId: UUID, request: ModifyMemberRequest):ModifyPasswordCommand {
            return ModifyPasswordCommand(
                resourceId = resourceId,
                password = request.password?:"",
                newPassword = request.newPassword?:""
            )
        }
    }
}