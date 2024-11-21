package kr.respectme.message.infrastructure.msa.impl

import kr.respectme.common.response.ApiResult
import kr.respectme.common.response.CursorList
import kr.respectme.message.infrastructure.msa.MemberQueryPort
import kr.respectme.message.infrastructure.msa.dto.Member
import kr.respectme.message.infrastructure.msa.dto.MemberQueryRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@Service
@FeignClient(name = "member-query", url = "\${respect-me.msa.member-api.url}")
interface HttpMemberQueryAdapter: MemberQueryPort {

    @PostMapping("/internal/api/v1/members")
    override fun loadMembers(@RequestBody request: MemberQueryRequest): ApiResult<CursorList<Member>>
}
