package kr.respectme.group.application.query

import kr.respectme.group.application.dto.notification.NotificationCountDto
import kr.respectme.group.application.dto.notification.NotificationQueryModelDto
import kr.respectme.group.application.query.useCase.NotificationQueryUseCase
import kr.respectme.group.port.out.persistence.QueryNotificationPort
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationQueryService(
    private val notificationRepository: QueryNotificationPort
): NotificationQueryUseCase {

    private final val GROUP_ALARM_LIMIT_PER_DAYS = 5

    override fun getNotification(memberId: UUID, notificationId: UUID): NotificationQueryModelDto {
        TODO("Not yet implemented")
    }

    override fun getNotifications(memberId: UUID, groupId: UUID, pageable: Pageable): List<NotificationQueryModelDto> {
        TODO("Not yet implemented")
    }

    override fun getTodayNotificationCount(groupId: UUID): NotificationCountDto {

        return NotificationCountDto(
            groupId = groupId,
            count = GROUP_ALARM_LIMIT_PER_DAYS - notificationRepository.findTodayNotificationCount(groupId)
        )
    }
}