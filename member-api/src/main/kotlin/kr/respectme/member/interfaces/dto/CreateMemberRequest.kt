package kr.respectme.member.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

@Schema(description = "회원 가입 요청")
class CreateMemberRequest(
    @field: NotBlank(message = "Nickname should not be empty string or null.")
    @field: Length(min=4, max=64, message = "Nickname length should be in range 4~64.")
    @Schema(required = true, example = "Lion King", description = "닉네임", minLength = 4, maxLength = 64)
    val nickname: String="",
    @field: Email(message = "Invalid email format.")
    @field: Length(min=4, max=64, message = "Email length should be in range 4~64.")
    @Schema(required = true, example = "email", description = "이메일", minLength = 4, maxLength = 64)
    val email: String="",
    @field: NotBlank(message = "Password should not be empty string or null.")
    @Schema(required = true, example = "password", description = "비밀번호")
    val password: String=""
) {

}
