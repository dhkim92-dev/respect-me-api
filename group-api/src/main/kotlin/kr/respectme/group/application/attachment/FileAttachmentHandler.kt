package kr.respectme.group.application.attachment

import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.attachment.Attachment
import kr.respectme.group.domain.attachment.AttachmentType
import kr.respectme.group.port.out.persistence.LoadAttachmentPort
import kr.respectme.group.port.out.persistence.LoadNotificationPort
import kr.respectme.group.port.out.persistence.SaveAttachmentPort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


@Service
class FileAttachmentHandler(
    private val loadNotificationPort: LoadNotificationPort,
    private val loadAttachmentPort: LoadAttachmentPort,
    private val saveAttachmentPort: SaveAttachmentPort,
): AttachmentHandler {

    /**
     * File Type Attachment를 Notificatio에 링크합니다.
     * @param notificationId Notification ID
     * @param attachment Attachment ID
     * @return AttachmentDto
     */
    @Transactional
    override fun linkAttachment(loginId: UUID, command: LinkAttachmentCommand): AttachmentDto {
        val notification = loadNotificationPort.loadEntityById(command.notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if ( notification.getSenderId() != loginId ) throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)

        val attachment = loadAttachmentPort.loadByResourceIdAndNotificationId(
            command.resourceId,
            command.notificationId
        )

        if ( attachment != null ) return AttachmentDto.of(attachment)

        val newAttachment = saveAttachmentPort.persist(Attachment(
            resourceId = command.resourceId,
            notificationId = command.notificationId,
            groupId = command.groupId,
            type = command.type
        ))

        return AttachmentDto.of(newAttachment)
    }


    override fun isSupport(command: LinkAttachmentCommand): Boolean {
        return isSupport(command.type)
    }

    override fun isSupport(type: AttachmentType): Boolean {
        return type == AttachmentType.FILE
    }
}