package kr.respectme.member.applications.dto

import kr.respectme.member.domain.model.DeviceTokenType
import kr.respectme.member.interfaces.dto.RegisterDeviceTokenRequest

data class RegisterDeviceTokenCommand(
    val type: DeviceTokenType,
    val token: String
) {

    companion object {
        fun of(request: RegisterDeviceTokenRequest): RegisterDeviceTokenCommand {
            return RegisterDeviceTokenCommand(
                type = request.type,
                token = request.token
            )
        }
    }
}