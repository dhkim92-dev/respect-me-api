package kr.respectme.member.port.`in`

import kr.respectme.member.port.`in`.dto.DeviceTokenResponse
import java.util.UUID

interface DeviceTokenQueryPort {

    fun getDeviceToken(loginId: UUID, memberId: UUID, resourceId: UUID): DeviceTokenResponse

    fun getDeviceTokens(loginId: UUID, memberId: UUID): List<DeviceTokenResponse>
}