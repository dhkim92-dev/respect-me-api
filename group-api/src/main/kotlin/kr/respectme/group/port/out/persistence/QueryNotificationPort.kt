package kr.respectme.group.port.out.persistence

import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.application.dto.notification.NotificationQueryModelDto
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import java.util.*

interface QueryNotificationPort {

    fun findNotificationById(notificationId: UUID): GroupNotificationQueryModel?

    fun findByNotificationsByGroupId(groupId: UUID, cursorId:UUID, pageable: Pageable): Slice<GroupNotificationQueryModel>

    fun findTodayNotificationCount(groupId: UUID): Int
}