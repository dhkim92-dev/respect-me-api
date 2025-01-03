package kr.respectme.auth.adapter.out.persistence.member

import kr.respectme.auth.port.out.persistence.member.dto.Member
import kr.respectme.auth.port.out.persistence.member.MemberLoadPort
import kr.respectme.common.response.ApiResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.*

@FeignClient(name = "member-feign-client", url = "\${respect-me.msa.member-api.url}")
interface RestMemberLoadAdapter : MemberLoadPort {

//    @GetMapping(value = ["/internal/api/v1/members/login"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
//    override fun loadMemberByEmailAndPassword(@RequestBody request: LoginRequest): ApiResult<Member?>

    @GetMapping(value = ["/internal/api/v1/members/{memberId}"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    override fun loadMemberById(@PathVariable memberId: UUID): ApiResult<Member?>
}