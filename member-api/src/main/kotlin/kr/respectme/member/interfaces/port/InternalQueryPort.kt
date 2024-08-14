package kr.respectme.member.interfaces.port

import kr.respectme.member.interfaces.dto.LoginRequest
import kr.respectme.member.interfaces.dto.MemberResponse
import java.util.UUID

interface InternalQueryPort {

    fun getMemberWithPassword(serviceAccountId: UUID, request: LoginRequest): MemberResponse

    fun getMember(serviceAccountId: UUID, memberId: UUID): MemberResponse
}