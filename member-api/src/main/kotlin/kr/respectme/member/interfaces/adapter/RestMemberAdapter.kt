package kr.respectme.member.interfaces.adapter

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
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
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
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
@Tag(name = "Member", description = "회원 API")
@SecurityRequirement(name = "bearer-jwt")
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

    @Operation(summary = "회원 가입", description = "email/password 기반 회원 가입")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "회원 가입 성공"),
    ])
    @PostMapping
    @ApplicationResponse(status= CREATED, message = "join member success.")
    override fun createMember(@RequestBody request: CreateMemberRequest)
    : MemberResponse {
        return MemberResponse.of(memberUseCase.join(CreateMemberCommand.of(request)))
    }

    @Operation(summary = "닉네임 변경", description = "닉네임 변경")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "닉네임 변경 성공"),
    ])
    @PatchMapping("/{memberId}/nickname")
    @ApplicationResponse(OK, "update nickname success.")
    override fun updateNickname(
        @LoginMember loginId: UUID,
        @PathVariable memberId: UUID,
        @RequestBody @Valid request: ModifyMemberRequest)
    : MemberResponse {
        return MemberResponse.of(
            memberUseCase.changeNickname(loginId, ModifyNicknameCommand.of(memberId, request))
        )
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
    ])
    @PatchMapping("/{memberId}/password")
    @ApplicationResponse(OK, "update password success")
    override fun updatePassword(
        @LoginMember loginId: UUID,
        @PathVariable memberId: UUID,
        @RequestBody @Valid request: ModifyMemberRequest)
    : MemberResponse {
        return MemberResponse.of(
            memberUseCase.changePassword(loginId, ModifyPasswordCommand.of(memberId, request))
        )
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
    ])
    @DeleteMapping("/{memberId}")
//    @ApplicationResponse(MemberServiceResultCode::class, "DELETE_MEMBER_SUCCESS")
    override fun deleteMember(
        @LoginMember loginId: UUID,
        @PathVariable memberId: UUID) {
        return memberUseCase.leave(loginId, memberId)
    }

    @Operation(summary = "회원 정보 단건 조회", description = "회원 정보 단건 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원 조회 성공"),
    ])
    @GetMapping("/{memberId}")
    @ApplicationResponse(OK, "get member success")
    override fun getMember(
        @LoginMember loginId: UUID,
        @PathVariable memberId: UUID
    ): MemberResponse {
        return MemberResponse.of(memberUseCase.getMember(loginId, memberId))
    }

    @Operation(summary = "회원 정보 다건 조회", description = "회원 정보 다건 조회")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원 조회 성공"),
    ])
    @GetMapping
    @ApplicationResponse(OK, "get members success")
    override fun getMembers(
        @LoginMember loginId: UUID,
        @RequestBody @Valid request: MembersQueryRequest
    ): List<MemberResponse> {
        return memberUseCase.getMembers(loginId, request.memberIds)
            .map { memberDto -> MemberResponse.of(memberDto)}
    }
}