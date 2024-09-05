package kr.respectme.member.interfaces.port

import kr.respectme.member.interfaces.dto.*
import java.util.UUID

interface MemberQueryPort {

    fun getMember(loginId: UUID, resourceId: UUID): MemberResponse

    fun getMembers(loginId: UUID, request: MembersQueryRequest): List<MemberResponse>

//    fun loginWithPassword(loginRequest: LoginRequest): MemberResponse
}