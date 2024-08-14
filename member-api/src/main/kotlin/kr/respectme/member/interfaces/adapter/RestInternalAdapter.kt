package kr.respectme.member.interfaces.adapter

import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.ServiceAccount
import kr.respectme.member.applications.dto.LoginCommand
import kr.respectme.member.applications.port.MemberUseCase
import kr.respectme.member.interfaces.dto.LoginRequest
import kr.respectme.member.interfaces.dto.MemberResponse
import kr.respectme.member.interfaces.port.InternalQueryPort
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/internal/api/v1/members")
class RestInternalAdapter(private val memberUseCase: MemberUseCase): InternalQueryPort {

    @PostMapping("/login")
    @ApplicationResponse(status=200, message = "login success.")
    override fun getMemberWithPassword(
        @ServiceAccount serviceAccountId: UUID,
        @RequestBody request: LoginRequest)
    : MemberResponse {
        return MemberResponse.of(memberUseCase.login(LoginCommand.of(request)))
    }

    @GetMapping("/{memberId}")
    @ApplicationResponse(status = 200, message = "get member success.")
    override fun getMember(
        @ServiceAccount serviceAccountId: UUID,
        @PathVariable memberId: UUID
    ): MemberResponse {
        val member = memberUseCase.getMember(memberId, memberId)
        return MemberResponse.of(member)
    }
}