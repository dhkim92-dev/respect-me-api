package kr.respectme.group.application.dto.notification

import java.util.*

data class NotificationCountDto(
    val groupId: UUID,
    val count: Int
) {

}