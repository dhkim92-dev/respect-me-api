package kr.respectme.group.application.dto.group

import kr.respectme.group.domain.GroupType
import kr.respectme.group.interfaces.dto.GroupModifyRequest
import java.util.*

data class GroupModifyCommand(
    val name: String?,
    val description: String?,
    val type: GroupType?,
    val password: String?,
    val ownerId: UUID?
) {

    companion object {

        fun of(request: GroupModifyRequest): GroupModifyCommand {
            return GroupModifyCommand(
                name = request.name,
                description = request.description,
                type = request.type,
                password = request.password,
                ownerId = request.ownerId
            )
        }
    }
}