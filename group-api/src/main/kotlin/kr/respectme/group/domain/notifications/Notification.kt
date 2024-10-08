package kr.respectme.group.domain.notifications

import kr.respectme.common.error.BadRequestException
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.notifications.NotificationType.*
import java.time.Instant
import java.util.*

abstract class Notification(
    val id: UUID = UUIDV7Generator.generate(),
    val groupId: UUID,
    val senderId: UUID,
    content: String = "",
    status : NotificationStatus = NotificationStatus.PENDING,
    type: NotificationType = IMMEDIATE,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant? = null,
    lastSentAt: Instant? = null
){

    var status: NotificationStatus = status
        private set

    var type: NotificationType = type
        private set

    var content: String = content
        private set

    val createdAt: Instant = createdAt

    var updatedAt: Instant? = updatedAt
        private set

    var lastSentAt: Instant? = lastSentAt
        private set

    fun updateContent(content: String?) {

        if(status == NotificationStatus.PUBLISHED) {
            throw BadRequestException(GroupServiceErrorCode.GROUP_NOTIFICATION_CANNOT_UPDATE_CONTENTS)
        }

        content?.let {
            this.content = content
            this.updatedAt = Instant.now()
        }
    }

    fun updateStatus(status: NotificationStatus?) {
        status?.takeIf { it != this.status } ?: return
        this.status = status
        updateTime()
    }

    fun switchType(type: NotificationType) {
        this.type = type
        updateTime()
    }

    protected fun updateTime() {
        this.updatedAt = Instant.now()
    }

    fun validate() {
        validateContent()
        validateType()
    }

    private fun validateContent() {
        if (content.isBlank()) {
            throw IllegalArgumentException("Notification content should not be empty")
        }
    }

    abstract fun validateType()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Notification) return false

        if (groupId != other.groupId) return false
        if (senderId != other.senderId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = groupId.hashCode()
        result = 31 * result + senderId.hashCode()
        return result
    }
}