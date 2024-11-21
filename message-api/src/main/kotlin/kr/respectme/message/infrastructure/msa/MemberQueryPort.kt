package kr.respectme.message.infrastructure.msa

import kr.respectme.common.response.ApiResult
import kr.respectme.common.response.CursorList
import kr.respectme.message.infrastructure.msa.dto.Member
import kr.respectme.message.infrastructure.msa.dto.MemberDto
import kr.respectme.message.infrastructure.msa.dto.MemberQueryRequest
import java.util.UUID

interface MemberQueryPort {

    fun loadMembers(request: MemberQueryRequest): ApiResult<CursorList<Member>>
}