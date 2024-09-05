package kr.respectme.group.interfaces.dto

import kr.respectme.group.application.dto.notification.NotificationDto
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant
import java.util.*

data class GroupNotificationVo(
    val notificationId: UUID,
    val groupId: UUID,
    val contents: String,
    val type: NotificationType,
    val status: NotificationStatus,
    val createdAt: Instant,
    val updatedAt: Instant?,
    val scheduledAt: Instant? = null,
    val dayOfWeeks: List<DayOfWeek>? = null,
    val dayInterval: Int? = null,
    val lastSentAt: Instant? = null,
) {

    companion object {

        fun valueOf(notification: NotificationDto): GroupNotificationVo {
            return GroupNotificationVo(
                notificationId = notification.notificationId,
                groupId = notification.groupId,
                contents = notification.content,
                type = notification.type,
                status = notification.state,
                createdAt = notification.createdAt,
                updatedAt = notification.updatedAt,
                scheduledAt = notification.scheduledAt,
                dayOfWeeks = notification.dayOfWeeks?.let{ DayOfWeek.toList(it) },
                dayInterval = notification.dayInterval,
                lastSentAt = notification.lastSentAt
            )
        }
    }
}