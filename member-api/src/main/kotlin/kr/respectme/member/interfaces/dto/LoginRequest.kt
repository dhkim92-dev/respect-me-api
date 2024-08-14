package kr.respectme.member.interfaces.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field: Email(message = "Email form required.")
    val email: String,
    @field: NotBlank(message = "Password required.")
    @field: Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters.")
    val password: String
){

}