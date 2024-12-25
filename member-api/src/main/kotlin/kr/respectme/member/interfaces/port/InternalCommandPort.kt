package kr.respectme.member.interfaces.port

import kr.respectme.member.interfaces.dto.CreateMemberRequest
import kr.respectme.member.interfaces.dto.MemberResponse
import java.util.UUID

interface InternalCommandPort {

    fun createMember(serviceAccountId: UUID, request: CreateMemberRequest): MemberResponse
}