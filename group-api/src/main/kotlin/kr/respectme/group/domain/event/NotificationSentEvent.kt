package kr.respectme.group.domain.event

import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

data class NotificationSentEvent(
    val notificationId: UUID,
    val groupId: UUID,
    val senderId: UUID,
    val receiverIds: List<UUID>,
    val contents: String,
    val createdAt: Instant
) {
    companion object {
        val name: String = "notification-sent-event"
    }
}