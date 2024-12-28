package kr.respectme.auth.port.`in`.persistence

import kr.respectme.auth.port.`in`.msa.members.dto.CreateMemberRequest
import kr.respectme.auth.port.`in`.msa.members.dto.Member
import kr.respectme.common.response.ApiResult
import java.util.UUID

interface MemberSavePort {

    fun registerMember(request: CreateMemberRequest): ApiResult<Member>

    fun deleteMember(memberId: UUID)
}