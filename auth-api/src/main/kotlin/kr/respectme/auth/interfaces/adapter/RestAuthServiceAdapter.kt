package kr.respectme.auth.interfaces.adapter

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.respectme.auth.application.dto.JwtAccessTokenVerifierRequiredInfo
import kr.respectme.auth.application.useCase.AuthUseCase
import kr.respectme.auth.infrastructures.dto.LoginRequest
import kr.respectme.auth.interfaces.dto.LoginResponse
import kr.respectme.auth.interfaces.dto.RefreshAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenResponse
import kr.respectme.auth.interfaces.port.AuthServicePort
import kr.respectme.common.annotation.ApplicationResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
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

    @Operation(summary = "JWT Access Token 유효성 검사에 필요한 정보를 조회", description = "각 서비스에서 JWT Token의 유효성 검사를 진행하기 위해 필요한 정보를 조회합니다.")
    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "JWT Token 유효성 검사 정보 조회 성공")])
    @GetMapping("/jwt/verification/requirements")
    @ApplicationResponse(status=OK, message = "retrieve access token verification requirements success.")
    override fun retrieveAccessTokenVerificationRequirements(@RequestHeader("Authorization") jwtToken: String): JwtAccessTokenVerifierRequiredInfo {
        val result = authUseCase.retrieveAccessTokenVerifierRequiredInfo(jwtToken)
        return result
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
//
//    @Operation(summary = "JWT Access Token 유효성 검사", description = "Access Token의 유효성 검사")
//    @ApiResponses(value = [ApiResponse(responseCode = "200", description = "JWT Token 유효성 검사 성공")])
//    @PostMapping("/jwt/verify")
//    @ApplicationResponse(status=OK, message = "validate access token success.")
//    override fun verifyAccessToken(@RequestBody @Valid request: VerifyAccessTokenRequest): VerifyAccessTokenResponse {
//        val result = authUseCase.validateToken(request.accessToken)
//        return VerifyAccessTokenResponse.of(result)
//    }
}