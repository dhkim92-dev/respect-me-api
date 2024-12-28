package kr.respectme.auth.infrastructures.adapters

import jakarta.validation.Valid
import kr.respectme.auth.port.`in`.msa.members.dto.CreateMemberRequest
import kr.respectme.auth.port.`in`.msa.members.dto.Member
import kr.respectme.auth.port.`in`.persistence.MemberSavePort
import kr.respectme.common.response.ApiResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.*

@FeignClient(name = "member-feign-save-client", url = "\${respect-me.msa.member-api.url}")
interface RestMemberSaveAdapter: MemberSavePort {

    @PostMapping(value = ["/internal/api/v1/members/registration"], consumes = [APPLICATION_JSON_VALUE], produces = [APPLICATION_JSON_VALUE])
    override fun registerMember(@RequestBody @Valid request: CreateMemberRequest): ApiResult<Member>

    @DeleteMapping(value = ["/internal/api/v1/members/{memberId}"])
    @Retryable(
        value = [Exception::class],
        maxAttempts = 3,
        backoff = Backoff(delay = 1000, maxDelay = 3000, multiplier = 2.0)
    )
    override fun deleteMember(@PathVariable memberId: UUID)
}