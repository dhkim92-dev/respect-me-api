package kr.respectme.message.infrastructure.event.dto

import java.util.*

class NotificationSentEvent(
    val groupId: UUID,
    val notificationId: UUID,
    val result: Boolean = true,
    val error: String = "",
    val successCount: Int = 0,
    val failureCount: Int = 0
) {

}