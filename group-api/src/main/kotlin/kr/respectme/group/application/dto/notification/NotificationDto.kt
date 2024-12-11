package kr.respectme.group.application.dto.notification

import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaImmediateNotification
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaScheduledNotification
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

    companion object {

        fun valueOf(entity: JpaImmediateNotification): NotificationDto {
            return NotificationDto(
                notificationId = entity.identifier,
                groupId = entity.groupId,
                content = entity.content,
                type = entity.type,
                state = entity.status,
                scheduledAt = null,
                dayOfWeeks = null,
                dayInterval = null,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                lastSentAt = entity.lastSentAt,
            )
        }

        fun valueOf(entity: JpaScheduledNotification): NotificationDto {
            return NotificationDto(
                notificationId = entity.identifier,
                groupId = entity.groupId,
                content = entity.content,
                type = entity.type,
                state = entity.status,
                scheduledAt = entity.scheduledAt,
                dayOfWeeks = null,
                dayInterval = null,
                createdAt = entity.createdAt,
                updatedAt = entity.updatedAt,
                lastSentAt = entity.lastSentAt,
            )
        }
    }
}