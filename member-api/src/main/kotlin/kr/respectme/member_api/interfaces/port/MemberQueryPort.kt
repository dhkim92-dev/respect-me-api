package kr.respectme.member_api.interfaces.port

import kr.respectme.member_api.domain.model.Member
import kr.respectme.member_api.interfaces.dto.CreateMemberRequest
import kr.respectme.member_api.interfaces.dto.MemberDetailResponse
import kr.respectme.member_api.interfaces.dto.MemberResponse
import kr.respectme.member_api.interfaces.dto.QueryMembersRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import java.util.UUID

interface MemberQueryPort {

    fun getMember(loginMember: Member, resourceId: UUID): MemberResponse

    fun getMembers(loginMember: Member, request: QueryMembersRequest): List<MemberResponse>


}