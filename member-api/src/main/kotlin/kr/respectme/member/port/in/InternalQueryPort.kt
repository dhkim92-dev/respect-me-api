package kr.respectme.member.port.`in`

import kr.respectme.member.port.`in`.dto.MemberResponse
import kr.respectme.member.port.`in`.dto.MembersQueryRequest
import java.util.UUID

interface InternalQueryPort {

    fun getMember(serviceAccountId: UUID, memberId: UUID): MemberResponse

    fun getMembersWithIds(serviceAccountId: UUID, request: MembersQueryRequest): List<MemberResponse>
}