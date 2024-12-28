package kr.respectme.auth.port.out.persistence.member

import kr.respectme.auth.port.out.persistence.member.dto.CreateMemberRequest
import kr.respectme.auth.port.out.persistence.member.dto.Member
import kr.respectme.common.response.ApiResult
import java.util.UUID

interface MemberSavePort {

    fun registerMember(request: CreateMemberRequest): ApiResult<Member>

    fun deleteMember(memberId: UUID)
}