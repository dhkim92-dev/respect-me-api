package kr.respectme.member.applications.dto

import kr.respectme.member.interfaces.dto.CreateMemberRequest

data class CreateMemberCommand(
    val nickname: String,
    val email: String,
    val password: String
) {

    companion object {

        fun of(request: CreateMemberRequest): CreateMemberCommand {
            return CreateMemberCommand(
                nickname = request.nickname,
                email = request.email,
                password = request.password
            )
        }
    }
}