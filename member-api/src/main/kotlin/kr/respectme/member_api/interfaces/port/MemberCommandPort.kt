package kr.respectme.member_api.interfaces.port

import kr.respectme.member_api.domain.model.Member
import kr.respectme.member_api.interfaces.dto.CreateMemberRequest
import kr.respectme.member_api.interfaces.dto.MemberResponse
import kr.respectme.member_api.interfaces.dto.ModifyMemberRequest
import java.util.*

interface MemberCommandPort {

    fun createMember(request: CreateMemberRequest): MemberResponse

    fun updateNickname(loginMember: Member, resourceId: UUID, request: ModifyMemberRequest): MemberResponse

    fun updatePassword(loginMember: Member, resourceId: UUID, request: ModifyMemberRequest): MemberResponse

    fun deleteMember(loginMember: Member, resourceId: UUID): Unit
}