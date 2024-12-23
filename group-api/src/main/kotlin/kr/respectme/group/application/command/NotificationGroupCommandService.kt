package kr.respectme.group.application.command

import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.ConflictException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.command.useCase.NotificationGroupUseCase
import kr.respectme.group.application.dto.group.GroupCreateCommand
import kr.respectme.group.application.dto.group.GroupModifyCommand
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.application.dto.member.GroupMemberCreateCommand
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationCreateCommand
import kr.respectme.group.application.dto.notification.NotificationCreateResult
import kr.respectme.group.application.dto.notification.NotificationModifyCommand
import kr.respectme.group.common.errors.GroupServiceErrorCode.*
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.port.out.event.dto.NotificationCreateEvent
import kr.respectme.group.domain.notifications.*
import kr.respectme.group.domain.notifications.factory.ImmediateNotificationFactory
import kr.respectme.group.domain.notifications.factory.NotificationFactory
import kr.respectme.group.domain.notifications.factory.ScheduledNotificationFactory
import kr.respectme.group.port.out.event.EventPublishPort
import kr.respectme.group.port.out.persistence.LoadGroupPort
import kr.respectme.group.port.out.persistence.SaveGroupPort
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationGroupCommandService(
    private val eventPublishPort: EventPublishPort,
    private val loadGroupPort: LoadGroupPort,
    private val saveGroupPort: SaveGroupPort,
    private val passwordEncoder: PasswordEncoder
): NotificationGroupUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun createNotificationGroup(loginId: UUID, command: GroupCreateCommand): NotificationGroupDto {
        val group = NotificationGroup(
            ownerId = loginId,
            name = command.groupName,
            description = command.groupDescription,
            type = command.groupType,
            password = command.groupPassword
        )
        logger.info("[GroupCreateEvent] group create request member : ${loginId}\n command : $command")
        group.changePassword(passwordEncoder, command.groupPassword)
        group.addMember(GroupMember(loginId, group.id, "group-owner", GroupMemberRole.OWNER))
        val savedGroup = saveGroupPort.save(group)
        logger.info("[GroupCreateEvent] platform member : ${loginId} created new group : ${savedGroup.id} group name: ${savedGroup.name}")
        return NotificationGroupDto.valueOf(savedGroup)
    }

    @Transactional
    override fun updateNotificationGroup(loginId: UUID, groupId: UUID, command: GroupModifyCommand): NotificationGroupDto {
        val group = loadGroupPort.loadGroup(
            groupId = groupId,
            memberIds = listOf(loginId)
        ) ?: throw NotFoundException(GROUP_NOT_FOUND)
        logger.info("[GroupModifyEvent] group modify request member : ${loginId}\n command : $command")
        val member = group.members.find { it.memberId == loginId } ?: throw NotFoundException(GROUP_MEMBER_NOT_FOUND)

        if(member.isGroupMember()) {
            logger.info("[GroupModifyEvent] group modify request rejected, member : ${loginId} is not group owner or admin.")
            throw ForbiddenException(GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        group.changeGroupName(command.name)
        group.changeGroupType(command.type)
        group.changeGroupDescription(command.description)
        group.changePassword(passwordEncoder, command.password)
        logger.info("[GroupModifyEvent] group modify request accepted, modified by member : ${loginId}" +
                "group name : ${group.name} group type : ${group.type} group description : ${group.description}")

        return NotificationGroupDto.valueOf(saveGroupPort.save(group))
    }

    @Transactional
    override fun addMember(loginId: UUID, groupId: UUID, command: GroupMemberCreateCommand)
    : GroupMemberDto {
        logger.info("[GroupMemberAddEvent] group member add request member : ${loginId}\n command : $command")
        val group = loadGroupPort.loadGroup(
            groupId = groupId,
            memberIds = listOf(loginId)
        ) ?: throw NotFoundException(GROUP_NOT_FOUND)

        val member = group.members.find { it.memberId == loginId }
        if(member != null) {
            logger.error("[GroupMemberAddEvent] group member add request rejected, member : ${loginId} already exists in group.")
            throw ConflictException(GROUP_MEMBER_ALREADY_EXISTS)
        }

        if(group.password != null && !passwordEncoder.matches(command.password, group.password)) {
            logger.error("[GroupMemberAddEvent] group member add request rejected, member : ${loginId} password mismatch.")
            throw ForbiddenException(GROUP_PASSWORD_MISMATCH)
        }

        // TODO Redis Lock 적용 필요
        // 사용자 가입 요청 처리 중 사용자가 플랫폼에서 탈퇴한다면 유령 회원이 생긴다.

        val newMember = GroupMember(
            memberId = loginId,
            groupId = groupId,
            nickname = command.nickname,
        )
        group.addMember(newMember)
        saveGroupPort.save(group)
        logger.info("[GroupMemberAddEvent] group member add request accepted, member : ${loginId} added to group ${groupId} group name : ${group.name}.")
        return GroupMemberDto.valueOf(newMember)
    }

    @Transactional
    override fun deleteNotificationGroup(loginId: UUID, groupId: UUID) {
        logger.info("[GroupDeleteEvent] group delete request member : ${loginId} group id : ${groupId}")
        val group = loadGroupPort.loadGroup(groupId)
            ?: throw NotFoundException(GROUP_NOT_FOUND)
        if(group.ownerId != loginId) {
            logger.error("[GroupDeleteEvent] group delete request rejected, member : ${loginId} is not group owner. owner Id ${group.ownerId}")
            throw ForbiddenException(GROUP_MEMBER_NOT_OWNER)
        }
        saveGroupPort.delete(group)
        logger.info("[GroupDeleteEvent] group delete request accepted, member : ${loginId} deleted group ${groupId} group name : ${group.name}.")
    }

    @Transactional
    override fun removeMember(loginId: UUID, groupId: UUID, memberIdToRemove: UUID) {
        logger.info("[GroupMemberRemoveEvent] group member remove request member : ${loginId} group id : ${groupId} target member id : ${memberIdToRemove}")
        // TODO Redis Lock 적용 필요
        val group = loadGroupPort.loadGroup(groupId, listOf(loginId, memberIdToRemove)) ?: throw NotFoundException(GROUP_NOT_FOUND)
        group.removeMember(loginId, memberIdToRemove)
        logger.info("[GroupMemberRemoveEvent] group member remove request accepted, member : ${loginId} removed member : ${memberIdToRemove} from group ${groupId} group name : ${group.name}.")
        saveGroupPort.save(group)
    }

    @Transactional
    override fun createNotification(loginId: UUID, groupId: UUID, command: NotificationCreateCommand)
    : NotificationCreateResult {
        logger.info("[NotificationCreateEvent] notification create request member : ${loginId} group id : ${groupId} command : $command")
        val group = loadGroupPort.loadGroup(groupId, listOf(loginId))
            ?: throw NotFoundException(GROUP_NOT_FOUND)
        val notificationFactory = getNotificationFactory(command.type)
        val notification = notificationFactory.build(command)
        group.addNotification(loginId, notification)
        val savedGroup = saveGroupPort.save(group)
        publishNotificationSentEvent(savedGroup, notification)

        logger.info("[NotificationCreateEvent] notification create request accepted, member : ${loginId} created notification : ${notification.id} in group ${groupId} group name : ${group.name}.")
        return NotificationCreateResult.valueOf(notification)
    }

    @Transactional
    override fun modifyNotificationContents(
        loginId: UUID,
        groupId: UUID,
        command: NotificationModifyCommand
    ): NotificationCreateResult {
        logger.info("[NotificationModifyContentsEvent] notification modify request member : ${loginId} group id : ${groupId} command : $command")
        val group = loadGroupPort.loadGroup(
            groupId = groupId,
            memberIds = listOf(loginId),
            notificationIds = listOf(command.notificationId)
        ) ?: throw NotFoundException(GROUP_NOT_FOUND)
        val member = group.members.find { it.memberId == loginId }
            ?: throw NotFoundException(GROUP_MEMBER_NOT_FOUND)

        val notification = group.notifications.find { it.id == command.notificationId }
            ?: throw NotFoundException(GROUP_NOTIFICATION_CONTENTS_EMPTY)

        if( (!member.isGroupOwner() && !member.isGroupAdmin()) || !member.isSameMember(notification.senderId) ) {
            throw ForbiddenException(GROUP_NOTIFICATION_SENDER_MISMATCH)
        }

        notification.updateContent(command.contents)
        notification.validate()
        saveGroupPort.save(group)
        logger.info("[NotificationModifyContentsEvent] notification modify request accepted, member : ${loginId} modified notification : ${notification.id} in group ${groupId} group name : ${group.name}.")
        return NotificationCreateResult.valueOf(notification)
    }

    @Transactional
    override fun modifyNotificationType(
        loginId: UUID,
        groupId: UUID,
        notificationId: UUID,
        command: NotificationModifyCommand
    ): NotificationCreateResult {
        logger.info("[NotificationModifyTypeEvent] notification modify request member : ${loginId} group id : ${groupId} command : $command")
        val group = loadGroupPort.loadGroup(
            groupId = groupId,
            memberIds = listOf(loginId),
            notificationIds = listOf(notificationId)
        ) ?: throw NotFoundException(GROUP_NOT_FOUND)
        val member = group.members.find { it.memberId == loginId }
            ?: throw NotFoundException(GROUP_MEMBER_NOT_FOUND)
        val notification = group.notifications.find { it.id == command.notificationId }
            ?: throw NotFoundException(GROUP_NOTIFICATION_CONTENTS_EMPTY)

        if(!member.isGroupOwner() && !member.isGroupAdmin()) {
            logger.error("[NotificationModifyTypeEvent] notification modify request rejected, member : ${loginId} is not group owner or admin.")
            throw ForbiddenException(GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        switchNotificationType(notification, command)
        notification.validate()
        saveGroupPort.save(group)
        logger.info("[NotificationModifyTypeEvent] notification modify request accepted, member : ${loginId} modified notification : ${notification.id} in group ${groupId} group name : ${group.name}.")
        return NotificationCreateResult.valueOf(notification)
    }


    private fun publishNotificationSentEvent(group: NotificationGroup, notification: Notification) {
        if(notification.type != NotificationType.IMMEDIATE) return

        val receivers = group.members.filter { it.memberId != notification.senderId }
        val event = NotificationCreateEvent(
            groupId = notification.groupId,
            groupName = group.name,
            notificationId = notification.id,
            contents = notification.content.take(100),
            createdAt = notification.createdAt,
            receiverIds = loadGroupPort.loadGroupMemberIds(notification.groupId),
            senderId = notification.senderId
        )

        try {
            eventPublishPort.publish(NotificationCreateEvent.eventName, event)
        } catch(e: Exception) {
            // TODO 재전송 로직 필요.
            logger.error("[NotificationCreateEvent] failed to publish event, event : $event")
        }
    }

    private fun getNotificationFactory(type: NotificationType): NotificationFactory {
        return when(type) {
            NotificationType.IMMEDIATE -> ImmediateNotificationFactory
            NotificationType.SCHEDULED -> ScheduledNotificationFactory
            else -> throw BadRequestException(GROUP_NOTIFICATION_INVALID_TYPE)
        }
    }

    private fun switchNotificationType(notification: Notification, command: NotificationModifyCommand) {
        if(command.type == null || notification.type == command.type) {
            return
        }

        when(command.type) {
            NotificationType.IMMEDIATE -> {
                notification.switchType(NotificationType.IMMEDIATE)
                notification as ImmediateNotification
            }

            NotificationType.SCHEDULED -> {
                notification as ScheduledNotification
                notification.setScheduledTime(command.scheduledAt)
            }
            else -> {
                throw BadRequestException(GROUP_NOTIFICATION_INVALID_TYPE)
            }
        }
    }
}