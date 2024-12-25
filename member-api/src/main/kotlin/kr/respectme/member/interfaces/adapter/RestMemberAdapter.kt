package kr.respectme.member.interfaces.adapter

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.LoginMember
import kr.respectme.member.applications.dto.CreateMemberCommand
import kr.respectme.member.applications.dto.ModifyNicknameCommand
import kr.respectme.member.applications.dto.RegisterDeviceTokenCommand
import kr.respectme.member.applications.port.command.DeviceTokenCommandUseCase
import kr.respectme.member.applications.port.command.MemberCommandUseCase
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
    private val memberUseCase: MemberCommandUseCase,
    private val deviceTokenUseCase: DeviceTokenCommandUseCase
): MemberQueryPort, MemberCommandPort {

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
    ])
    @DeleteMapping("/{memberId}")
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

    @Operation(summary = "회원 알람 수신 디바이스 토큰 삭제", description = "회원 알람 수신 디바이스 삭제")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "디바이스 삭제 성공"),
    ])
    @DeleteMapping("/{memberId}/device-tokens/{tokenId}")
    fun deleteDeviceToken(@LoginMember loginId: UUID,
                          @PathVariable memberId: UUID,
                          @PathVariable tokenId: UUID) {
        return deviceTokenUseCase.deleteDeviceToken(loginId, memberId, tokenId)
    }

    @Operation(summary = "회원 알람 수신 디바이스 토큰 등록", description = "회원 알람 수신 디바이스 토큰 등록")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "디바이스 토큰 등록 성공"),
    ])
    @PostMapping("/{memberId}/device-tokens")
    @ApplicationResponse(CREATED, "register device token success")
    fun refreshDeviceToken(@LoginMember loginId: UUID,
                           @PathVariable memberId: UUID,
                           @RequestBody @Valid request: RegisterDeviceTokenRequest): DeviceTokenResponse {
        return DeviceTokenResponse.of(
            deviceTokenUseCase.registerDeviceToken(loginId, memberId, RegisterDeviceTokenCommand.of(request))
        )
    }
}