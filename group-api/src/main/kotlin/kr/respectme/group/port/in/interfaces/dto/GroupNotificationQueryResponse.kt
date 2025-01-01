package kr.respectme.group.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.group.application.dto.notification.NotificationDto
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.port.`in`.interfaces.vo.Writer
import java.time.Instant
import java.util.*

@Schema(description = "그룹 Notification 정보")
data class GroupNotificationVo(
    @Schema(description = "Notification ID")
    val notificationId: UUID,
    @Schema(description = "그룹 ID")
    val groupId: UUID,
    @Schema(description = "작성자 정보")
    val writer: Writer = Writer(),
    @Schema(description = "Notification 내용")
    val contents: String,
    @Schema(description = "Notification 타입", examples = ["IMMEDIATE", "SCHEDULED", "REPEATED_WEEKLY", "REPEATED_INTERVAL"])
    val type: NotificationType,
    @Schema(description = "Notification 상태", example = "PENDING")
    val status: NotificationStatus,
    @Schema(description = "Notification 생성 일시")
    val createdAt: Instant,
    @Schema(description = "Notification 수정 일시")
    val updatedAt: Instant?,
    @Schema(description = "Notification 예약 일시(TYPE = SCHEDULED)의 경우")
    val scheduledAt: Instant? = null,
    @Schema(description = "Notification 요일, 매주 반복일 경우(TYPE = REPEATED_WEEKLY)")
    val dayOfWeeks: List<DayOfWeek>? = null,
    @Schema(description = "Notification 간격, 매 n일 반복일 경우(TYPE = REPEATED_INTERVAL)")
    val dayInterval: Int? = null,
    @Schema(description = "Notification 마지막 전송 일시")
    val lastSentAt: Instant? = null,
) {

    companion object {

        fun valueOf(notification: NotificationDto): GroupNotificationVo {
            return GroupNotificationVo(
                notificationId = notification.notificationId,
                groupId = notification.groupId,
                writer = notification.writer,
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