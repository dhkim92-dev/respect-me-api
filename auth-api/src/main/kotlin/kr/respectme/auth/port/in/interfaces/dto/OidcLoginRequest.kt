package kr.respectme.auth.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.respectme.auth.domain.OidcPlatform

@Schema(description = "OIDC 로그인 요청 객체")
data class OidcLoginRequest(
    @field: NotEmpty
    @Schema(description = "OIDC 플랫폼 프로바이더가 제공한 ID Token, Firebase ID Token 사용 불가", required = true, example = "Google ID Token Or Apple ID Token")
    val idToken: String
) {

}