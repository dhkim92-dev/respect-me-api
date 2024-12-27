package kr.respectme.member.port.`in`

import kr.respectme.member.port.`in`.dto.MemberResponse
import kr.respectme.member.port.`in`.dto.MembersQueryRequest
import java.util.UUID

interface MemberQueryPort {

    fun getMember(loginId: UUID, resourceId: UUID): MemberResponse

    fun getMembers(loginId: UUID, request: MembersQueryRequest): List<MemberResponse>

//    fun loginWithPassword(loginRequest: LoginRequest): MemberResponse
}