package kr.respectme.message.infrastructure.event.dto

import java.time.Instant
import java.util.UUID

class NotificationCreateEvent(
    var notificationId: UUID = UUID.randomUUID(),
    var groupId: UUID = UUID.randomUUID(),
    val groupImageUrl:String? = null,
    var groupName: String = "",
    var senderId: UUID = UUID.randomUUID(),
    var receiverIds: List<UUID> = emptyList(),
    var title: String? = null,
    var contents: String = "",
    var createdAt: Instant = Instant.now()
) {

}