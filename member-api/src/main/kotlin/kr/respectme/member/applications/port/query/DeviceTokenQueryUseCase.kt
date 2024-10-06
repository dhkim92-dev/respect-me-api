package kr.respectme.member.applications.port.query

import kr.respectme.member.applications.dto.DeviceTokenDto
import java.util.UUID

interface DeviceTokenQueryUseCase {

    fun retrieveDeviceTokens(loginId: UUID, memberId: UUID): List<DeviceTokenDto>

    fun retrieveDeviceToken(loginId: UUID, memberId: UUID, tokenId: UUID): DeviceTokenDto
}