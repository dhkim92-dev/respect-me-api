package kr.respectme.member.interfaces.port

import kr.respectme.member.interfaces.dto.CreateMemberRequest
import kr.respectme.member.interfaces.dto.MemberResponse
import kr.respectme.member.interfaces.dto.ModifyMemberRequest
import java.util.*

interface MemberCommandPort {

    fun deleteMember(loginId: UUID, resourceId: UUID): Unit
}