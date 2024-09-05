package kr.respectme.group.interfaces.dto

import kr.respectme.group.application.dto.notification.NotificationCreateResult
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant
import java.util.*

data class NotificationResponse(
    val groupId: UUID,
    val senderId: UUID,
    val notificationId: UUID,
    val state: NotificationStatus,
    val type: NotificationType,
    val content: String,
    val createdAt: Instant,
    val lastSentAt: Instant? = null,
    val scheduledAt: Instant? = null,
    val dayOfWeeks: List<DayOfWeek>? = null,
    val dayInterval: Int? = null,
) {
    companion object {
        fun valueOf(result: NotificationCreateResult): NotificationResponse {
            return NotificationResponse(
                groupId = result.groupId,
                senderId = result.senderId,
                notificationId = result.notificationId,
                state = result.status,
                type = result.type,
                createdAt = result.createdAt,
                scheduledAt = result.scheduledAt,
                dayOfWeeks = result.dayOfWeeks?.let{DayOfWeek.toList(it)},
                dayInterval = result.dayInterval,
                content = result.content,
            )
        }
    }
}