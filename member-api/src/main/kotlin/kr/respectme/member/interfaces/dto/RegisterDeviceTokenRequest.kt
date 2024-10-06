package kr.respectme.member.interfaces.dto

import jakarta.validation.constraints.NotBlank
import kr.respectme.member.domain.model.DeviceTokenType

data class RegisterDeviceTokenRequest(
    val type: DeviceTokenType,
    @field: NotBlank(message = "token must not be blank or null")
    val token: String
) {
}