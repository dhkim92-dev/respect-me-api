package kr.respectme.group.application.query

import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationDto
import kr.respectme.group.application.query.useCase.NotificationGroupQueryUseCase
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.common.errors.GroupServiceErrorCode.GROUP_MEMBER_NOT_MEMBER
import kr.respectme.group.common.errors.GroupServiceErrorCode.GROUP_NOT_FOUND
import kr.respectme.group.domain.GroupType
import kr.respectme.group.port.out.persistence.QueryGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationGroupQueryService(
    private val queryGroupPort: QueryGroupPort,
): NotificationGroupQueryUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

//    @Transactional(readOnly = true)
//    fun retrieveMemberNotifications(loginId: UUID, cursor: UUID?, size: Int): List<NotificationDto> {
//        return queryGroupPort.getMemberNotifications(loginId, cursor, size+1)
//            .sortedBy { it.notificationId }
//            .reversed()
//    }

    @Transactional(readOnly = true)
    override fun retrieveGroupNotifications(
        loginId: UUID,
        groupId: UUID,
        cursor: UUID?,
        size: Int
    ): List<NotificationDto> {
        val groupMember = queryGroupPort.getGroupMember(groupId, loginId)
            ?: throw ForbiddenException(GROUP_MEMBER_NOT_MEMBER)
        logger.info("group member : ${groupMember.memberId} query group $groupId members")
        return queryGroupPort.getPublishedNotifications(groupId, cursor, size+1)
            .sortedBy { it.notificationId }
            .reversed()
    }

    @Transactional(readOnly = true)
    override fun retrieveGroupMembers(loginId: UUID, groupId: UUID): List<GroupMemberDto> {
        val groupMember = queryGroupPort.getGroupMember(groupId, loginId)
            ?: throw ForbiddenException(GROUP_MEMBER_NOT_MEMBER)

        return queryGroupPort.getGroupMembers(groupId)
            .sortedBy { it.memberId }
            .reversed()
    }

    @Transactional(readOnly = true)
    override fun retrieveGroupMember(loginId: UUID, groupId: UUID, memberId: UUID): GroupMemberDto {
        return queryGroupPort.getGroupMember(groupId, loginId)
            ?: throw ForbiddenException(GROUP_MEMBER_NOT_MEMBER)
    }

    @Transactional(readOnly = true)
    override fun retrieveGroup(loginId: UUID, groupId: UUID): NotificationGroupDto {
        val group = queryGroupPort.getGroup(groupId)
            ?: throw NotFoundException(GROUP_NOT_FOUND)
        if(group.groupType == GroupType.GROUP_PRIVATE) {
            val groupMember = queryGroupPort.getGroupMember(groupId, loginId)
                ?: throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_MEMBER)
        }

        return group
    }

    @Transactional(readOnly = true)
    override fun retrieveMemberGroups(loginId: UUID): List<NotificationGroupDto> {
        return queryGroupPort.getMemberGroups(loginId)
            .sortedBy { it.id }
            .reversed()
    }

    @Transactional(readOnly = true)
    override fun retrieveAllGroups(loginId: UUID, cursorGroupId: UUID?, size: Int?): List<NotificationGroupDto> {
        return queryGroupPort.getAllGroups(cursorGroupId, size ?: 21)
    }

    @Transactional(readOnly = true)
    override fun retrieveMemberNotifications(loginId: UUID, cursor: UUID?, size: Int): List<NotificationDto> {
        return queryGroupPort.getMemberNotifications(loginId, cursor, size+1)
    }

    @Transactional(readOnly = true)
    override fun retrieveNotification(loginId: UUID, groupId: UUID, notificationId: UUID): NotificationDto {
        val groupMember = queryGroupPort.getGroupMember(groupId, loginId)
            ?: throw ForbiddenException(GROUP_MEMBER_NOT_MEMBER)
        val notification = queryGroupPort.getNotification(groupId, notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)
        return notification
    }
}