package kr.respectme.group.application.attachment

import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.error.UnsupportedMediaTypeException
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.port.out.persistence.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class LinkAttachmentManager(
    private val handlers: List<AttachmentHandler>,
    private val notificationPort: LoadNotificationPort,
    private val loadMemberPort: LoadMemberPort,
    private val loadAttachmentPort: LoadAttachmentPort,
    private val saveAttachmentPort: SaveAttachmentPort
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun getAttachments(loginId: UUID, groupId: UUID, notificationId: UUID): List<AttachmentDto> {
        val notification = notificationPort.loadEntityById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)
        val member = loadMemberPort.load(groupId, loginId)
            ?: throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)

        return loadAttachmentPort.loadByNotificationId(notificationId)
            .map { AttachmentDto.of(it) }
    }

    @Transactional
    fun link(loginId: UUID, request: LinkAttachmentCommand)
            : AttachmentDto {
        return handlers.find { handler -> handler.isSupport(request) }
            ?.linkAttachment(loginId, command = request)
            ?: throw UnsupportedMediaTypeException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_SUPPORTED_ATTACHMENT_TYPE)
    }

    @Transactional
    fun unlink(loginId: UUID, notificationId: UUID, attachmentId: UUID) {
        val notification = notificationPort.loadEntityById(notificationId)
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if (notification.getSenderId() != loginId) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_SENDER_MISMATCH)
        }

        val attachment = loadAttachmentPort.loadById(attachmentId)
            ?: return;

        if ( attachment.notificationId != notificationId ) {
            throw BadRequestException(GroupServiceErrorCode.GROUP_NOTIFICATION_ATTACHMENT_NOTIFICATION_ID_MISMATCH)
        }

        saveAttachmentPort.delete(attachment)
    }
}