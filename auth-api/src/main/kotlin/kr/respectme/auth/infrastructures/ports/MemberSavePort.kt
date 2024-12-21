package kr.respectme.auth.infrastructures.ports

import kr.respectme.auth.infrastructures.dto.CreateMemberRequest
import kr.respectme.auth.infrastructures.dto.Member
import kr.respectme.common.response.ApiResult

interface MemberSavePort {

    fun registerMember(request: CreateMemberRequest): ApiResult<Member>
}