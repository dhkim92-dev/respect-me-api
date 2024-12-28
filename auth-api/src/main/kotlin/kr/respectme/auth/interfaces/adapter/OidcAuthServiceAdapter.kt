package kr.respectme.auth.interfaces.adapter

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import kr.respectme.auth.application.dto.OidcMemberLoginCommand
import kr.respectme.auth.application.useCase.OidcAuthUseCase
import kr.respectme.auth.domain.OidcPlatform
import kr.respectme.auth.port.`in`.interfaces.dto.LoginResponse
import kr.respectme.auth.port.`in`.interfaces.dto.OidcLoginRequest
import kr.respectme.auth.port.`in`.interfaces.OidcAuthPort
import kr.respectme.common.annotation.ApplicationResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth/oidc")
class OidcAuthServiceAdapter(
    private val oidcAuthUseCase: OidcAuthUseCase
): OidcAuthPort {

    @PostMapping("/google")
    @Operation(summary = "OIDC Google 로그인", description = """Google OIDC 로그인<br/> 
        Google의 ID Token을 직접 전달해야합니다.<br/> 
        Firebase ID Token은 사용할 수 없습니다.<br/>
    """)
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Google OIDC 로그인 성공"),
    ])
    @ApplicationResponse(status = HttpStatus.CREATED, message = "login with google success.")
    override fun loginWithGoogle(@RequestBody @Valid request: OidcLoginRequest): LoginResponse {
        val result = oidcAuthUseCase.loginWithOidc(OidcMemberLoginCommand(
            platform = OidcPlatform.GOOGLE,
            idToken = request.idToken
        ))

        return LoginResponse.of(result)
    }

    @PostMapping("/apple")
    @Operation(summary = "OIDC Apple 로그인", description = """Apple OIDC 로그인<br/> 
        Apple의 ID Token을 직접 전달해야합니다.<br/> 
        Firebase ID Token은 사용할 수 없습니다.<br/>
    """)
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Apple OIDC 로그인 성공"),
    ])
    @ApplicationResponse(status = HttpStatus.CREATED, message = "login with apple success.")
    override fun loginWithApple(@RequestBody request: OidcLoginRequest): LoginResponse {
        val result = oidcAuthUseCase.loginWithOidc(OidcMemberLoginCommand(
            platform = OidcPlatform.APPLE,
            idToken = request.idToken,
        ))

        return LoginResponse.of(result)
    }
}