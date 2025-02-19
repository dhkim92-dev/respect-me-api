package kr.respectme.group.application.command

import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.command.useCase.NotificationCommandUseCase
import kr.respectme.group.application.dto.notification.NotificationCreateCommand
import kr.respectme.group.application.dto.notification.NotificationCreateResult
import kr.respectme.group.application.dto.notification.NotificationModifyCommand
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.port.out.event.EventPublishPort
import kr.respectme.group.port.out.event.dto.NotificationCreateEvent
import kr.respectme.group.port.out.persistence.LoadGroupPort
import kr.respectme.group.port.out.persistence.LoadMemberPort
import kr.respectme.group.port.out.persistence.LoadNotificationPort
import kr.respectme.group.port.out.persistence.SaveNotificationPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationCommandService(
    private val loadGroupPort: LoadGroupPort,
    private val loadMemberPort: LoadMemberPort,
    private val saveNotificationPort: SaveNotificationPort,
    private val loadNotificationPort: LoadNotificationPort,
    private val eventPublishPort: EventPublishPort
)
: NotificationCommandUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)
    private final val MAX_NOTIFICATION_COUNT_PER_DAY = 5

    @Transactional
    override fun createNotification(
        loginId: UUID,
        groupId: UUID,
        command: NotificationCreateCommand
    ): NotificationCreateResult {
        val group = loadGroupPort.loadGroup(groupId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOT_FOUND)
        val member = loadMemberPort.load(groupId, loginId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        if(member.isGroupMember()) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        val leftNotificationCount = loadNotificationPort.countTodayGroupNotification(groupId)

        if(leftNotificationCount > MAX_NOTIFICATION_COUNT_PER_DAY) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_EXCEED_LIMIT)
        }

        val notification = Notification(
            groupId = groupId,
            senderId = loginId,
            content = command.content,
            type = command.type,
            status = NotificationStatus.PUBLISHED
        )

        saveNotificationPort.saveNotification(notification)
        val receivers = loadMemberPort.findMemberIdsByGroupId(groupId)
            .filter { it != notification.getSenderId() }
        val event = NotificationCreateEvent.valueOf(group, notification, receivers)
        eventPublishPort.publish(NotificationCreateEvent.eventName, event)

        return NotificationCreateResult.valueOf(notification)
    }

    @Transactional
    override fun updateNotification(
        loginId: UUID,
        groupId: UUID,
        notificationId: UUID,
        command: NotificationModifyCommand
    ): NotificationCreateResult {

        var notification = loadNotificationPort.loadEntityById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if(notification.getGroupId() != groupId) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_GROUP_ID_MISMATCH)
        }

        if(notification.getSenderId() != loginId) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_MEMBER_ID_MISTMATCH)
        }

        notification.updateContent(command.content)
        notification = saveNotificationPort.saveNotification(notification)

        return NotificationCreateResult.valueOf(notification)
    }

    @Transactional
    override fun deleteNotification(loginId: UUID, groupId: UUID, notificationId: UUID) {
        val notification = loadNotificationPort.loadEntityById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if(notification.getGroupId() != groupId) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_GROUP_ID_MISMATCH)
        }

        val member = loadMemberPort.load(groupId, loginId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        if(member.isGroupMember()) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        saveNotificationPort.deleteNotification(notification)
    }
}