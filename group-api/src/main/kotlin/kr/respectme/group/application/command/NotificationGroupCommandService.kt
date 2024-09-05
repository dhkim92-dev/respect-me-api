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
import kr.respectme.group.domain.event.NotificationSentEvent
import kr.respectme.group.domain.notifications.*
import kr.respectme.group.domain.notifications.factory.ImmediateNotificationFactory
import kr.respectme.group.domain.notifications.factory.NotificationFactory
import kr.respectme.group.domain.notifications.factory.ScheduledNotificationFactory
import kr.respectme.group.infrastructures.event.EventPublishPort
import kr.respectme.group.infrastructures.persistence.port.LoadGroupPort
import kr.respectme.group.infrastructures.persistence.port.SaveGroupPort
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
        )
        logger.info("group create request member : ${loginId}\n command : $command")
        group.changePassword(passwordEncoder, command.groupPassword)
        group.addMember(GroupMember(loginId, group.id, "group-owner", GroupMemberRole.OWNER))
        val savedGroup = saveGroupPort.save(group)
        logger.info("platform user : ${loginId} created new group : ${savedGroup.id} groupName: ${savedGroup.name}")
        return NotificationGroupDto.valueOf(savedGroup)
    }

    @Transactional
    override fun updateNotificationGroup(loginId: UUID, groupId: UUID, command: GroupModifyCommand): NotificationGroupDto {
        val group = loadGroupPort.loadGroup(groupId)
            ?: throw NotFoundException(GROUP_NOT_FOUND)
        val member = group.members.find { it.memberId == loginId } ?: throw NotFoundException(GROUP_MEMBER_NOT_FOUND)

        if(member.isGroupMember()) {
            throw ForbiddenException(GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        group.changeGroupName(command.name)
        group.changeGroupType(command.type)
        group.changeGroupDescription(command.description)
        command.ownerId?.let {
            group.changeGroupOwner(loginId, command.ownerId)
        }
        group.changePassword(passwordEncoder, command.password)

        return NotificationGroupDto.valueOf(saveGroupPort.save(group))
    }

    @Transactional
    override fun addMember(loginId: UUID, groupId: UUID, command: GroupMemberCreateCommand)
    : GroupMemberDto {
        val group = loadGroupPort.loadGroup(groupId)
            ?: throw NotFoundException(GROUP_NOT_FOUND)
        val member = group.members.find { it.memberId == loginId }
        if(member != null) {
            throw ConflictException(GROUP_MEMBER_ALREADY_EXISTS)
        }

        if(group.password != null && !passwordEncoder.matches(command.password, group.password)) {
            throw ForbiddenException(GROUP_PASSWORD_MISMATCH)
        }

        val newMember = GroupMember(
            memberId = loginId,
            groupId = groupId,
            nickname = command.nickname,
        )
        group.addMember(newMember)
        saveGroupPort.save(group)
        return GroupMemberDto.valueOf(newMember)
    }

    @Transactional
    override fun deleteNotificationGroup(memberId: UUID, groupId: UUID) {
        val group = loadGroupPort.loadGroup(groupId)
            ?: throw NotFoundException(GROUP_NOT_FOUND)
        val member = group.members.find { it.memberId == memberId }
            ?: throw NotFoundException(GROUP_MEMBER_NOT_FOUND)
        if(!member.isGroupOwner()) throw ForbiddenException(GROUP_MEMBER_NOT_OWNER)

        saveGroupPort.delete(group)
    }

    @Transactional
    override fun removeMember(loginId: UUID, groupId: UUID, memberIdToRemove: UUID) {
        val group = loadGroupPort.loadGroup(groupId) ?: throw NotFoundException(GROUP_NOT_FOUND)
        group.removeMember(loginId, memberIdToRemove)
        saveGroupPort.save(group)
    }

    @Transactional
    override fun createNotification(loginId: UUID, groupId: UUID, command: NotificationCreateCommand)
    : NotificationCreateResult {
        logger.info("create Notification Request: ${command}")
        val group = loadGroupPort.loadGroup(groupId) ?: throw NotFoundException(GROUP_NOT_FOUND)
        val notificationFactory = getNotificationFactory(command.type)
        val notification = notificationFactory.build(command)
        logger.info("notification created. notification : ${notification.content}")
        group.addNotification(loginId, notification)
        logger.info("notification added.")
        val savedGroup = saveGroupPort.save(group)
        publishNotificationSentEvent(savedGroup, notification)
        return NotificationCreateResult.valueOf(notification)
    }

    @Transactional
    override fun modifyNotificationContents(
        loginId: UUID,
        groupId: UUID,
        command: NotificationModifyCommand
    ): NotificationCreateResult {
        val group = loadGroupPort.loadGroup(groupId) ?: throw NotFoundException(GROUP_NOT_FOUND)
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
        return NotificationCreateResult.valueOf(notification)
    }

    @Transactional
    override fun modifyNotificationType(
        loginId: UUID,
        groupId: UUID,
        notificationId: UUID,
        command: NotificationModifyCommand
    ): NotificationCreateResult {
        val group = loadGroupPort.loadGroup(groupId) ?: throw NotFoundException(GROUP_NOT_FOUND)
        val member = group.members.find { it.memberId == loginId }
            ?: throw NotFoundException(GROUP_MEMBER_NOT_FOUND)
        val notification = group.notifications.find { it.id == command.notificationId }
            ?: throw NotFoundException(GROUP_NOTIFICATION_CONTENTS_EMPTY)

        if(!member.isGroupOwner() && !member.isGroupAdmin()) {
            throw ForbiddenException(GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        switchNotificationType(notification, command)
        notification.validate()
        saveGroupPort.save(group)
        return NotificationCreateResult.valueOf(notification)
    }


    private fun publishNotificationSentEvent(group: NotificationGroup, notification: Notification) {
        if(notification.type != NotificationType.IMMEDIATE) return

        val receivers = group.members.filter { it.memberId != notification.senderId }
        val event = NotificationSentEvent(
            notificationId = notification.id,
            contents = notification.content,
            createdAt = notification.createdAt,
            groupId = group.id,
            receiverIds = receivers.map { it.memberId },
            senderId = notification.senderId
        )

        eventPublishPort.publish(NotificationSentEvent.name, event)
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