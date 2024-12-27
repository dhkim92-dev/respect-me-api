package kr.respectme.member.applications.dto

import jakarta.validation.constraints.NotBlank
import kr.respectme.member.port.`in`.dto.ModifyMemberRequest
import java.util.*

data class ModifyNicknameCommand(
    val resourceId: UUID,
    @field: NotBlank(message = "nickname must not be null or empty.")
    val nickname: String,
) {

    companion object {

        fun of(resourceId: UUID, request: ModifyMemberRequest): ModifyNicknameCommand {
            return ModifyNicknameCommand(
                resourceId = resourceId,
                nickname = request.nickname?:"",
            )
        }
    }
}