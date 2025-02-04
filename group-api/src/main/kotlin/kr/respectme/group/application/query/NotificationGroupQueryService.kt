package kr.respectme.group.application.query

import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.dto.group.GroupQueryModelDto
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationDto
import kr.respectme.group.application.dto.notification.NotificationQueryModelDto
import kr.respectme.group.application.query.useCase.NotificationGroupQueryUseCase
import kr.respectme.group.common.errors.GroupServiceErrorCode.*
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

    /**
     * 회원의 그룹 알림 목록을 조회한다.
     * @param loginId 로그인 ID
     * @param groupId 그룹 ID
     * @param cursor 페이징 커서, CursorPagination의 next 필드에 포함된 cursor 값
     * @param size 조회할 알림 개수
     */
    @Transactional(readOnly = true)
    override fun retrieveGroupNotifications(
        loginId: UUID,
        groupId: UUID,
        cursor: UUID?,
        size: Int
    ): List<NotificationQueryModelDto> {
        val groupMember = queryGroupPort.getGroupMember(groupId, loginId)
            ?: throw ForbiddenException(GROUP_MEMBER_NOT_MEMBER)
        logger.info("group member : ${groupMember.memberId} query group $groupId members")
        return queryGroupPort.getPublishedNotifications(groupId, cursor, size+1)
            .sortedBy { it.id }
            .reversed()
            .map{ it -> NotificationQueryModelDto.valueOf(it) }
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
    override fun retrieveGroup(loginId: UUID, groupId: UUID): GroupQueryModelDto {
        val group = queryGroupPort.getGroup(loginId, groupId)
            ?: throw NotFoundException(GROUP_NOT_FOUND)

        if(group.type == GroupType.GROUP_PRIVATE) {
            val groupMember = queryGroupPort.getGroupMember(groupId, loginId)
                ?: throw ForbiddenException(GROUP_MEMBER_NOT_MEMBER)
        }

        return GroupQueryModelDto.valueOf(group)
    }

    @Transactional(readOnly = true)
    override fun retrieveMemberGroups(loginId: UUID): List<GroupQueryModelDto> {
        return queryGroupPort.getMemberGroups(loginId)
            .sortedBy { it.id }
            .reversed()
            .map{ GroupQueryModelDto.valueOf(it) }
    }

    @Transactional(readOnly = true)
    override fun retrieveAllGroups(loginId: UUID, cursorGroupId: UUID?, size: Int?): List<GroupQueryModelDto> {
        val querySize = size?.let{ it + 1 } ?: 21
        return queryGroupPort.getAllGroups(cursorGroupId, querySize)
            .sortedBy { it.id }
            .reversed()
            .map{ GroupQueryModelDto.valueOf(it) }
    }

    @Transactional(readOnly = true)
    override fun retrieveMemberNotifications(loginId: UUID, cursor: UUID?, size: Int): List<NotificationQueryModelDto> {
        return queryGroupPort.getMemberNotifications(loginId, cursor, size+1)
            .sortedBy { it.id }
            .reversed()
            .map { it -> NotificationQueryModelDto.valueOf(it) }
    }

    @Transactional(readOnly = true)
    override fun retrieveNotification(loginId: UUID, groupId: UUID, notificationId: UUID): NotificationQueryModelDto {
        logger.info("################### 단건 조회 콜 ####################")
        val groupMember = queryGroupPort.getGroupMember(groupId, loginId)
            ?: throw ForbiddenException(GROUP_MEMBER_NOT_MEMBER)

        val notification = queryGroupPort.getNotification(groupId, notificationId)
            ?: throw NotFoundException(GROUP_NOTIFICATION_NOT_EXISTS)
        return NotificationQueryModelDto.valueOf(notification)
    }
}