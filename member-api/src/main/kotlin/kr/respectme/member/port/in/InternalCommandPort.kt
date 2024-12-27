package kr.respectme.member.port.`in`

import kr.respectme.member.port.`in`.dto.CreateMemberRequest
import kr.respectme.member.port.`in`.dto.MemberResponse
import java.util.UUID

interface InternalCommandPort {

    fun createMember(serviceAccountId: UUID, request: CreateMemberRequest): MemberResponse

    fun deleteMember(serviceAccountId: UUID, memberId: UUID)
}