package kr.respectme.auth.interfaces.adapter

import io.micrometer.tracing.Tracer
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.respectme.auth.application.port.AuthUseCase
import kr.respectme.auth.infrastructures.dto.LoginRequest
import kr.respectme.auth.interfaces.dto.LoginResponse
import kr.respectme.auth.interfaces.dto.RefreshAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenResponse
import kr.respectme.auth.interfaces.port.AuthServicePort
import kr.respectme.common.annotation.ApplicationResponse
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "인증 API")
class RestAuthServiceAdapter(private val authUseCase: AuthUseCase): AuthServicePort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Operation(summary = "JWT Refresh Token과 Access Token 발급", description = "이메일/패스워드를 통한 토큰 획득")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "JWT Token 발급 성공"),
    ])
    @PostMapping("/jwt")
    @ApplicationResponse(status=CREATED, message = "create token success.")
    override fun login(@RequestBody @Valid request: LoginRequest): LoginResponse {
        val result = authUseCase.login(request)
        return LoginResponse.of(result)
    }

    @Operation(summary = "JWT Access Token 재발급", description = "Refresh Token을 통한 Access Token 재발급")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "JWT Token 재발급 성공"),
    ])
    @PostMapping("/jwt/reissue")
    @ApplicationResponse(status= CREATED, message = "refresh access token success.")
    override fun reissueAccessToken(@RequestBody @Valid request: RefreshAccessTokenRequest): LoginResponse {
        val result = authUseCase.refreshAccessToken(request.refreshToken)
        return LoginResponse.of(result)
    }

    @Operation(summary = "JWT Access Token 유효성 검사", description = "Access Token의 유효성 검사")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "JWT Token 유효성 검사 성공")])
    @PostMapping("/jwt/verify")
    @ApplicationResponse(status=OK, message = "validate access token success.")
    override fun verifyAccessToken(@RequestBody @Valid request: VerifyAccessTokenRequest): VerifyAccessTokenResponse {
        val result = authUseCase.validateToken(request.accessToken)
        return VerifyAccessTokenResponse.of(result)
    }
}