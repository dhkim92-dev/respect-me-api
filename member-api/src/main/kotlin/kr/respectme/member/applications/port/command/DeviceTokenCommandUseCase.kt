package kr.respectme.member.applications.port.command

import kr.respectme.member.applications.dto.DeviceTokenDto
import kr.respectme.member.applications.dto.RegisterDeviceTokenCommand
import java.util.UUID

interface DeviceTokenCommandUseCase {

    fun retrieveDeviceTokens(loginId: UUID, memberId: UUID): List<DeviceTokenDto>

    fun registerDeviceToken(loginId: UUID, memberId: UUID, command: RegisterDeviceTokenCommand): DeviceTokenDto

    fun deleteDeviceToken(loginId: UUID, memberId: UUID, tokenId: UUID): Unit
}