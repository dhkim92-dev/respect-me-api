package kr.respectme.auth.interfaces.adapter

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import kr.respectme.auth.application.dto.OidcMemberLoginCommand
import kr.respectme.auth.application.useCase.OidcAuthUseCase
import kr.respectme.auth.domain.OidcPlatform
import kr.respectme.auth.interfaces.dto.LoginResponse
import kr.respectme.auth.interfaces.dto.OidcLoginRequest
import kr.respectme.auth.interfaces.port.OidcAuthPort
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
        최초 요청에서 404 에러가 발생하면 nickname을 함께 전달하여 회원가입을 수행해야 합니다.
    """)
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Google OIDC 로그인 성공"),
        ApiResponse(responseCode = "404", description = "해당 사용자 정보로 가입된 회원 정보 및 인증 정보가 존재하지 않음")
    ])
    @ApplicationResponse(status = HttpStatus.CREATED, message = "login with google success.")
    override fun loginWithGoogle(@RequestBody @Valid request: OidcLoginRequest): LoginResponse {
        val result = oidcAuthUseCase.loginWithOidc(OidcMemberLoginCommand(
            platform = OidcPlatform.GOOGLE,
            idToken = request.idToken,
            nickname = request.nickname
        ))

        return LoginResponse.of(result)
    }

    @PostMapping("/apple")
    @Operation(summary = "OIDC Apple 로그인", description = """Apple OIDC 로그인<br/> 
        Apple의 ID Token을 직접 전달해야합니다.<br/> 
        Firebase ID Token은 사용할 수 없습니다.<br/>
        최초 요청에서 404 에러가 발생하면 nickname을 함께 전달하여 회원가입을 수행해야 합니다.
    """)
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Apple OIDC 로그인 성공"),
        ApiResponse(responseCode = "404", description = "해당 사용자 정보로 가입된 회원 정보 및 인증 정보가 존재하지 않음")
    ])
    @ApplicationResponse(status = HttpStatus.CREATED, message = "login with apple success.")
    override fun loginWithApple(request: OidcLoginRequest): LoginResponse {
        val result = oidcAuthUseCase.loginWithOidc(OidcMemberLoginCommand(
            platform = OidcPlatform.APPLE,
            idToken = request.idToken,
            nickname = request.nickname
        ))

        return LoginResponse.of(result)
    }
}