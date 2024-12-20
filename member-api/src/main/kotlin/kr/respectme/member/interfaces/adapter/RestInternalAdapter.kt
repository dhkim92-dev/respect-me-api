package kr.respectme.member.interfaces.adapter

import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.annotation.ServiceAccount
import kr.respectme.member.applications.dto.LoginCommand
import kr.respectme.member.applications.port.command.MemberCommandUseCase
import kr.respectme.member.interfaces.dto.LoginRequest
import kr.respectme.member.interfaces.dto.MemberResponse
import kr.respectme.member.interfaces.dto.MembersQueryRequest
import kr.respectme.member.interfaces.port.InternalQueryPort
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/internal/api/v1/members")
class RestInternalAdapter(private val memberUseCase: MemberCommandUseCase): InternalQueryPort {

    @PostMapping("/login")
    @ApplicationResponse(status=OK, message = "login success.")
    override fun getMemberWithPassword(
        @ServiceAccount serviceAccountId: UUID,
        @RequestBody request: LoginRequest)
    : MemberResponse {
        return MemberResponse.of(memberUseCase.login(LoginCommand.of(request)))
    }

    @GetMapping("/{memberId}")
    @ApplicationResponse(status = OK, message = "get member success.")
    override fun getMember(
        @ServiceAccount serviceAccountId: UUID,
        @PathVariable memberId: UUID
    ): MemberResponse {
        val member = memberUseCase.getMember(memberId, memberId)
        return MemberResponse.of(member)
    }

    @PostMapping("")
    @ApplicationResponse(status = OK, message = "get members with ids success.")
    @CursorPagination
    override fun getMembersWithIds(
        @ServiceAccount serviceAccountId: UUID,
        @RequestBody request: MembersQueryRequest
    ): List<MemberResponse> {
        return memberUseCase.getMembers(serviceAccountId, request.memberIds)
            .map { MemberResponse.of(it) }
    }
}