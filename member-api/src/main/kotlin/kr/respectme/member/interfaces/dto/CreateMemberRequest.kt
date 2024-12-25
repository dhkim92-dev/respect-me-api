package kr.respectme.member.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

@Schema(description = "회원 가입 요청")
class CreateMemberRequest(
    @field: Email(message = "Invalid email format.")
    @field: Length(min=4, max=64, message = "Email length should be in range 4~64.")
    @Schema(required = true, example = "email", description = "이메일", minLength = 4, maxLength = 64)
    val email: String="",
    @Schema(required = false, example = "password", description = "비밀번호, OIDC 회원 생성에서는 사용되지 않습니다.")
    val password: String?=null
) {

}
