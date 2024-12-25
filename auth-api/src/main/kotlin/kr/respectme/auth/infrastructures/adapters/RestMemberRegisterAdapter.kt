package kr.respectme.auth.infrastructures.adapters

import kr.respectme.auth.infrastructures.dto.CreateMemberRequest
import kr.respectme.auth.infrastructures.dto.Member
import kr.respectme.auth.infrastructures.ports.MemberSavePort
import kr.respectme.common.response.ApiResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "member-feign-save-client", url = "\${respect-me.msa.member-api.url}")
interface RestMemberRegisterAdapter: MemberSavePort {

    @PostMapping(value = ["/internal/api/v1/members"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    override fun registerMember(@RequestBody request: CreateMemberRequest): ApiResult<Member>
}