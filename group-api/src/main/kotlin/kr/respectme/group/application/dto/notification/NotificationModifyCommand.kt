package kr.respectme.group.application.dto.notification

import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationType
import java.time.Instant
import java.util.*

data class NotificationModifyCommand(
    val notificationId: UUID,
    val contents: String? = null,
    val type: NotificationType? = null,
    val scheduledAt: Instant? = null,
    val dayOfWeek: List<DayOfWeek>? = null,
    val dayInterval: Int? = null
) {

}