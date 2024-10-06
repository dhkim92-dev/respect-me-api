package kr.respectme.member.interfaces.adapter

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.LoginMember
import kr.respectme.member.applications.port.query.DeviceTokenQueryUseCase
import kr.respectme.member.interfaces.dto.DeviceTokenResponse
import kr.respectme.member.interfaces.port.DeviceTokenQueryPort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("api/v1/members/")
@Tag(name = "DeviceToken", description = "디바이스 토큰 조회 API")
@SecurityRequirement(name = "bearer-jwt")
class RestDeviceTokenQueryAdapter(
    private val deviceTokenQueryUseCase: DeviceTokenQueryUseCase
): DeviceTokenQueryPort {

    @Operation(summary = "retrieve registered device token", description = "retrieve registered device token by resourceId")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "디바이스 토큰 조회 성공"),
    ])
    @GetMapping("{memberId}/device-tokens/{resourceId}")
    @ApplicationResponse(status = HttpStatus.OK, message = "get device token success.")
    override fun getDeviceToken(
        @LoginMember loginId: UUID,
        @PathVariable(required = true) memberId: UUID,
        @PathVariable(required = true) resourceId: UUID
    ): DeviceTokenResponse {
        return DeviceTokenResponse.of(deviceTokenQueryUseCase.retrieveDeviceToken(loginId, memberId, resourceId))
    }

    @Operation(summary = "retrieve registered device tokens", description = "retrieve registered device tokens by loginId")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "get device tokens success"),
    ])
    @GetMapping("{memberId}/device-tokens")
    @ApplicationResponse(status = HttpStatus.OK, message = "get device tokens success.")
    @CursorPagination
    override fun getDeviceTokens(
        @LoginMember loginId: UUID,
        @PathVariable(required = true) memberId: UUID
    ): List<DeviceTokenResponse> {
        return deviceTokenQueryUseCase.retrieveDeviceTokens(loginId, memberId)
            .map { DeviceTokenResponse.of(it) }
    }
}