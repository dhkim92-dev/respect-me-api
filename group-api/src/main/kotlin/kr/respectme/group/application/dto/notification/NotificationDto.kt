package kr.respectme.group.application.dto.notification

import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant
import java.util.UUID

class NotificationDto(
    val notificationId: UUID,
    val groupId: UUID,
    val content: String,
    val type: NotificationType,
    val state: NotificationStatus,
    val scheduledAt: Instant?,
    val dayOfWeeks: Int?,
    val dayInterval: Int?,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val lastSentAt: Instant?,
) {

}