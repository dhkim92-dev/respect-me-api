package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.application.dto.notification.NotificationCountDto
import java.util.*

interface NotificationQueryPort {

    fun getTodayNotificationCount(loginId: UUID, groupId: UUID): NotificationCountDto
}