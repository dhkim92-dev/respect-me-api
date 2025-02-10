package kr.respectme.group.application.query

import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.application.dto.notification.NotificationCountDto
import kr.respectme.group.application.dto.notification.NotificationQueryModelDto
import kr.respectme.group.application.query.useCase.NotificationQueryUseCase
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.port.out.msa.file.LoadImagePort
import kr.respectme.group.port.out.msa.file.dto.LoadImagesRequest
import kr.respectme.group.port.out.persistence.LoadMemberPort
import kr.respectme.group.port.out.persistence.LoadNotificationPort
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class NotificationQueryService(
    private val loadNotificationPort: LoadNotificationPort,
    private val loadMemberPort: LoadMemberPort,
    private val loadImagePort: LoadImagePort
): NotificationQueryUseCase {

    private final val GROUP_ALARM_LIMIT_PER_DAYS = 5

    @Transactional(readOnly = true)
    override fun retrieveNotification(memberId: UUID, groupId: UUID, notificationId: UUID): NotificationQueryModelDto {
        val notification = loadNotificationPort.findNotificationById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if(notification.groupInfo.id != groupId) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_GROUP_ID_MISMATCH)
        }

        val member = loadMemberPort.findByGroupIdAndMemberId(notification.groupInfo.id, memberId)
            ?: throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_MEMBER)
        val response = try {
            loadImagePort.getImageInfos(LoadImagesRequest.valueOf(notification.groupInfo))
        } catch(e: Exception) {
            null
        }
        val thumbnail = response?.data?.firstOrNull()
        return NotificationQueryModelDto.valueOf(notification, thumbnail?.url)
    }

    @Transactional(readOnly = true)
    override fun retrieveGroupNotifications(memberId: UUID,
                                            groupId: UUID,
                                            cursorId: UUID?,
                                            size: Int): List<NotificationQueryModelDto> {
        val member = loadMemberPort.findByGroupIdAndMemberId(groupId, memberId)
            ?: throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_MEMBER)

        val notifications = loadNotificationPort.findNotificationsByGroupId(groupId, cursorId, PageRequest.of(0, size+1))
            .content

        val thumbnailIds = notifications.mapNotNull { it.groupInfo.imageId }
        val thumbnailMap = try {
            loadImagePort.getImageInfos(LoadImagesRequest(thumbnailIds))
                .data
                .associateBy({ it.id }, { it.url })
        } catch(e: Exception) {
            null
        }

        val result = notifications.map { NotificationQueryModelDto.valueOf(it, thumbnailMap?.get(it.groupInfo.imageId)) }
        return result
    }

    @Transactional(readOnly = true)
    override fun retrieveUnreadNotifications(
        memberId: UUID,
        cursorId: UUID?,
        size: Int
    ): List<NotificationQueryModelDto> {
        val notifications = loadNotificationPort.findByMemberId(memberId, cursorId, PageRequest.of(0, size+1))
            .content

        val thumbnailIds = notifications.mapNotNull { it.groupInfo.imageId }
        val thumbnailMap = try {
            loadImagePort.getImageInfos(LoadImagesRequest(thumbnailIds))
                .data
                .associateBy({ it.id }, { it.url })
        } catch(e: Exception) {
            null
        }

        val result = notifications.map { NotificationQueryModelDto.valueOf(it, thumbnailMap?.get(it.groupInfo.imageId)) }
        return result
    }

    @Transactional(readOnly = true)
    override fun retrieveTodayLeftNotificationCount(groupId: UUID): NotificationCountDto {
        val leftCount = GROUP_ALARM_LIMIT_PER_DAYS - loadNotificationPort.countTodayGroupNotification(groupId)
        return NotificationCountDto(
            groupId = groupId,
            count = if(leftCount >= 0) leftCount else 0
        )
    }
}