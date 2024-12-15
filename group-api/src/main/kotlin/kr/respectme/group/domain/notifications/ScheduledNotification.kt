package kr.respectme.group.domain.notifications

import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.common.errors.GroupServiceErrorCode.GROUP_NOTIFICATION_RESERVED_AT_INVALID
import java.time.Instant
import java.util.*

@DomainEntity
class ScheduledNotification(
    id : UUID = UUIDV7Generator.generate(),
    groupId : UUID = UUIDV7Generator.generate(),
    senderId : UUID = UUIDV7Generator.generate(),
    content : String = "",
    status : NotificationStatus = NotificationStatus.PENDING,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant? = null,
    lastSentAt: Instant? = null,
    scheduledAt: Instant = Instant.now()
) : Notification(
    id = id,
    groupId = groupId,
    senderId = senderId,
    content = content,
    status = status,
    type = NotificationType.SCHEDULED,
    createdAt = createdAt,
    updatedAt = updatedAt,
    lastSentAt = lastSentAt,
) {

    var scheduledAt: Instant = scheduledAt
        private set

    fun setScheduledTime(scheduledAt: Instant?) {
        scheduledAt?.let {
            this.scheduledAt = scheduledAt
            updateTime()
        } ?: throw BadRequestException(GROUP_NOTIFICATION_RESERVED_AT_INVALID)
    }

    override fun validateType() {
        if(type != NotificationType.SCHEDULED) {
            throw IllegalArgumentException("Notification type is not scheduled")
        }

        if(scheduledAt.isBefore(Instant.now())) {
            throw BadRequestException(GROUP_NOTIFICATION_RESERVED_AT_INVALID)
        }
    }
}