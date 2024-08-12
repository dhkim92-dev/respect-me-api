package kr.respectme.member_api.interfaces.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class CreateMemberRequest(

    @NotBlank(message = "Nickname should not be empty string or null.")
    @Length(min=4, max=64, message = "Nickname length should be in range 4~64.")
    val nickname: String,

    @Email(message = "Invalid email format.")
    @Length(min=4, max=64, message = "Email length should be in range 4~64.")
    val email: String,

    @NotBlank(message = "Password should not be empty string or null.")
    val password: String
) {

}
