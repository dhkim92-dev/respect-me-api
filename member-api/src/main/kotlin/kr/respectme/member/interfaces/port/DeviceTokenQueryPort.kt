package kr.respectme.member.interfaces.port

import kr.respectme.member.interfaces.dto.DeviceTokenResponse
import java.util.UUID

interface DeviceTokenQueryPort {

    fun getDeviceToken(loginId: UUID, memberId: UUID, resourceId: UUID): DeviceTokenResponse

    fun getDeviceTokens(loginId: UUID, memberId: UUID): List<DeviceTokenResponse>
}