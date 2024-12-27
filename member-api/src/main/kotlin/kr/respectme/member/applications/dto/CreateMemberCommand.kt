package kr.respectme.member.applications.dto

import kr.respectme.member.port.`in`.dto.CreateMemberRequest

data class CreateMemberCommand(
    val email: String,
    val password: String?
) {

    companion object {

        fun of(request: CreateMemberRequest): CreateMemberCommand {
            return CreateMemberCommand(
                email = request.email,
                password = request.password
            )
        }
    }
}