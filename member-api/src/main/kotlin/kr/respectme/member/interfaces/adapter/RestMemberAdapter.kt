package kr.respectme.member.interfaces.adapter

import jakarta.validation.Valid
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.LoginMember
import kr.respectme.member.applications.dto.CreateMemberCommand
import kr.respectme.member.applications.dto.ModifyNicknameCommand
import kr.respectme.member.applications.dto.ModifyPasswordCommand
import kr.respectme.member.applications.port.MemberUseCase
import kr.respectme.member.interfaces.dto.*
import kr.respectme.member.interfaces.port.MemberCommandPort
import kr.respectme.member.interfaces.port.MemberQueryPort
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api/v1/members")
class RestMemberAdapter(
    private val memberUseCase: MemberUseCase
): MemberQueryPort, MemberCommandPort {

//    @PostMapping("/login")
//    @ApplicationResponse(MemberServiceResultCode::class, "G")
//    override fun loginWithPassword(
//        @RequestBody @Valid request: LoginRequest
//    ): MemberResponse {
//        return MemberResponse.of(memberUseCase.login(LoginCommand.of(request)))
//    }

    @PostMapping
    @ApplicationResponse(status=201, message = "join member success.")
    override fun createMember(@RequestBody @Valid request: CreateMemberRequest)
    : MemberResponse {
        return MemberResponse.of(memberUseCase.join(CreateMemberCommand.of(request)))
    }

    @PatchMapping("/{resourceId}/nickname")
    @ApplicationResponse(200, "update nickname success.")
    override fun updateNickname(
        @LoginMember loginId: UUID,
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: ModifyMemberRequest)
    : MemberResponse {
        return MemberResponse.of(
            memberUseCase.changeNickname(loginId, ModifyNicknameCommand.of(resourceId, request))
        )
    }

    @PatchMapping("/{resourceId}/password")
    @ApplicationResponse(200, "update password success")
    override fun updatePassword(
        @LoginMember loginId: UUID,
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: ModifyMemberRequest)
    : MemberResponse {
        return MemberResponse.of(
            memberUseCase.changePassword(loginId, ModifyPasswordCommand.of(resourceId, request))
        )
    }

    @DeleteMapping("/{resourceId}")
//    @ApplicationResponse(MemberServiceResultCode::class, "DELETE_MEMBER_SUCCESS")
    override fun deleteMember(
        @LoginMember loginId: UUID,
        @PathVariable resourceId: UUID) {
        return memberUseCase.leave(loginId, resourceId)
    }

    @GetMapping("/{resourceId}")
    @ApplicationResponse(200, "get member success")
    override fun getMember(
        @LoginMember loginId: UUID,
        @PathVariable resourceId: UUID
    ): MemberResponse {
        return MemberResponse.of(memberUseCase.getMember(loginId, resourceId))
    }

    @GetMapping
    @ApplicationResponse(200, "get members success")
    override fun getMembers(
        @LoginMember loginId: UUID,
        @RequestBody @Valid request: QueryMembersRequest
    ): List<MemberResponse> {
        return memberUseCase.getMembers(loginId, request.memberIds)
            .map { memberDto -> MemberResponse.of(memberDto)}
    }
}