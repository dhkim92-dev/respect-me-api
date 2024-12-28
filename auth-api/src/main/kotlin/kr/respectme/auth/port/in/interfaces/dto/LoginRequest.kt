package kr.respectme.auth.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "email/password 로그인 요청")
class LoginRequest(
    @Schema(description = "이메일", example = "test@respect-me.kr", required = true)
    val email: String,
    @Schema(description = "비밀번호", example = "password", required = true)
    val password: String
) {
}