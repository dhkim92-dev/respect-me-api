package kr.respectme.group.application.dto.notification

import kr.respectme.group.domain.notifications.*
import java.time.Instant
import java.util.UUID

data class NotificationCreateResult(
    val notificationId: UUID,
    val groupId: UUID,
    val senderId: UUID,
    val content: String,
    val status: NotificationStatus,
    val type: NotificationType,
    val createdAt: Instant,
    val scheduledAt: Instant? = null,
    val lastSentAt: Instant? = null,
    val dayOfWeeks: Int? = null,
    val dayInterval: Int? = null

) {

    companion object {
        fun valueOf(notification: Notification): NotificationCreateResult {
            return when(notification) {
                is ImmediateNotification -> {
                    NotificationCreateResult(
                        notificationId = notification.id,
                        groupId = notification.groupId,
                        senderId = notification.senderId,
                        content = notification.content,
                        status = notification.status,
                        type = notification.type,
                        createdAt = notification.createdAt,
                        lastSentAt = notification.lastSentAt
                    )
                }
                is ScheduledNotification -> {
                    NotificationCreateResult(
                        notificationId = notification.id,
                        groupId = notification.groupId,
                        senderId = notification.senderId,
                        content = notification.content,
                        status = notification.status,
                        type = notification.type,
                        createdAt = notification.createdAt,
                        scheduledAt = notification.scheduledAt
                    )
                }
                else -> throw IllegalArgumentException("Invalid notification type")
            }
        }
    }
}