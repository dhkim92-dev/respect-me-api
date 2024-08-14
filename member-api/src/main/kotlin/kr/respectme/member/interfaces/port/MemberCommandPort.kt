package kr.respectme.member.interfaces.port

import kr.respectme.member.interfaces.dto.CreateMemberRequest
import kr.respectme.member.interfaces.dto.MemberResponse
import kr.respectme.member.interfaces.dto.ModifyMemberRequest
import java.util.*

interface MemberCommandPort {

    fun createMember(request: CreateMemberRequest): MemberResponse

    fun updateNickname(loginId: UUID, resourceId: UUID, request: ModifyMemberRequest): MemberResponse

    fun updatePassword(loginId: UUID, resourceId: UUID, request: ModifyMemberRequest): MemberResponse

    fun deleteMember(loginId: UUID, resourceId: UUID): Unit
}