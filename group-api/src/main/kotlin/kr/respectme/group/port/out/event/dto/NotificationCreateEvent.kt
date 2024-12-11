package kr.respectme.group.port.out.event.dto

import java.time.Instant
import java.util.UUID

data class NotificationCreateEvent(
    val notificationId: UUID,
    val groupId: UUID,
    val groupName: String,
    val senderId: UUID,
    val title: String? = null,
    val receiverIds: List<UUID>,
    val contents: String,
    val createdAt: Instant
) {

    companion object {
        val eventName = "notification-create-event"
    }
}