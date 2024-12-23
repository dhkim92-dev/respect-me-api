package kr.respectme.auth.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import kr.respectme.auth.domain.OidcPlatform

@Schema(description = "OIDC 로그인 요청 객체")
data class OidcLoginRequest(
    @field: NotEmpty
    @Schema(description = "OIDC 플랫폼 프로바이더가 제공한 ID Token, Firebase ID Token 사용 불가", required = true, example = "Google ID Token Or Apple ID Token")
    val idToken: String,
    @Schema(description = "OIDC ID Token으로 로그인 시도 시, 404 에러가 발생하면 해당 필드를 채워 다시 요청을 보내면 회원가입 로직과 함께 수행됨")
    val nickname: String?
) {

}