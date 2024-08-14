package kr.respectme.auth.interfaces.adapter

import jakarta.validation.Valid
import kr.respectme.auth.application.port.AuthenticationUseCase
import kr.respectme.auth.infrastructures.dto.LoginRequest
import kr.respectme.auth.interfaces.dto.LoginResponse
import kr.respectme.auth.interfaces.dto.RefreshAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenRequest
import kr.respectme.auth.interfaces.dto.VerifyAccessTokenResponse
import kr.respectme.auth.interfaces.port.AuthServicePort
import kr.respectme.common.annotation.ApplicationResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class RestAuthServiceAdapter(private val authenticationUseCase: AuthenticationUseCase): AuthServicePort {

    @PostMapping("/jwt")
    @ApplicationResponse(status=201, message = "create token success.")
    override fun login(@RequestBody @Valid request: LoginRequest): LoginResponse {
        val result = authenticationUseCase.login(request)
        return LoginResponse.of(result)
    }

    @PostMapping("/jwt/reissue")
    @ApplicationResponse(status=201, message = "refresh access token success.")
    override fun reissueAccessToken(@RequestBody @Valid request: RefreshAccessTokenRequest): LoginResponse {
        val result = authenticationUseCase.refreshAccessToken(request.refreshToken)
        return LoginResponse.of(result)
    }

    @PostMapping("/jwt/verify")
    @ApplicationResponse(status=201, message = "validate access token success.")
    override fun verifyAccessToken(@RequestBody @Valid request: VerifyAccessTokenRequest): VerifyAccessTokenResponse {
        val result = authenticationUseCase.validateToken(request.accessToken)
        return VerifyAccessTokenResponse.of(result)
    }
}