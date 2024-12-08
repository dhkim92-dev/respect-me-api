package kr.respectme.message.infrastructure.event.dto

import java.util.*

class NotificationSentEvent(
    val groupId: UUID,
    val notificationId: UUID,
    var result: Boolean = true,
    var error: String = "",
    val successCount: Int = 0,
    val failureCount: Int = 0
) {

}