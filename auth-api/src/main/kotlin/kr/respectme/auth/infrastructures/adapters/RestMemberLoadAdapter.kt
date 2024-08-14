package kr.respectme.auth.infrastructures.adapters

import kr.respectme.auth.infrastructures.dto.LoginRequest
import kr.respectme.auth.infrastructures.dto.Member
import kr.respectme.auth.infrastructures.ports.MemberLoadPort
import kr.respectme.common.response.ApiResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import java.util.*

@FeignClient(name = "member-feign-client", url = "\${msa.member-api.url}")
interface RestMemberLoadAdapter : MemberLoadPort {

    @GetMapping(value = ["/internal/api/v1/members/login"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    override fun getMemberByEmailAndPassword(@RequestBody request: LoginRequest): ApiResult<Member?>

    @GetMapping(value = ["/internal/api/v1/members/{memberId}"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    override fun getMemberById(@PathVariable memberId: UUID): ApiResult<Member?>
}