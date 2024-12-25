package kr.respectme.member.interfaces.adapter

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.ServiceAccount
import kr.respectme.member.applications.dto.CreateMemberCommand
import kr.respectme.member.applications.port.command.MemberCommandUseCase
import kr.respectme.member.interfaces.dto.CreateMemberRequest
import kr.respectme.member.interfaces.dto.MemberResponse
import kr.respectme.member.interfaces.dto.MembersQueryRequest
import kr.respectme.member.interfaces.port.InternalCommandPort
import kr.respectme.member.interfaces.port.InternalQueryPort
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/internal/api/v1/members")
class RestInternalAdapter(private val memberUseCase: MemberCommandUseCase): InternalQueryPort, InternalCommandPort {

    @GetMapping("/{memberId}")
    @Operation(summary = "회원 조회", description = "회원 조회")
    @ApiResponses(value =
        [ApiResponse(responseCode = "200", description = "회원 조회 성공")]
    )
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
    @ApiResponses(value =
        [ApiResponse(responseCode = "200", description = "회원 조회 성공")]
    )
    @Operation(summary = "회원 조회", description = "내부 서비스가 이용하는 회원 조회")
    @CursorPagination
    override fun getMembersWithIds(
        @ServiceAccount serviceAccountId: UUID,
        @RequestBody request: MembersQueryRequest
    ): List<MemberResponse> {
        return memberUseCase.getMembers(serviceAccountId, request.memberIds)
            .map { MemberResponse.of(it) }
    }

    @PostMapping("/registration")
    @ApplicationResponse(status = CREATED, message = "create member success.")
    @Operation(summary = "회원 가입", description = "내부 서비스용 회원 생성 서비스")
    override fun createMember(
        @ServiceAccount serviceAccountId: UUID,
        @RequestBody request: CreateMemberRequest
    ): MemberResponse {
        return MemberResponse.of(memberUseCase.join(
            CreateMemberCommand.of(request)
        ))
    }

    @DeleteMapping("/{memberId}")
    @ApplicationResponse(status = NO_CONTENT, message = "delete member success.")
    @Operation(summary = "회원 삭제", description = "내부 서비스용 회원 삭제 서비스")
    override fun deleteMember(
        @ServiceAccount serviceAccountId: UUID,
        @PathVariable memberId: UUID
    ) {
        memberUseCase.deleteMemberByService(memberId)
    }
}