package kr.respectme.member.domain.model

import kr.respectme.common.utility.UUIDV7Generator
import java.time.LocalDateTime
import java.util.UUID

data class DeviceToken(
    val tokenId: UUID = UUIDV7Generator.generate(),
    val memberId: UUID = UUID.randomUUID(),
    val tokenType: DeviceTokenType = DeviceTokenType.TYPE_FCM,
    val token: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
) {

}