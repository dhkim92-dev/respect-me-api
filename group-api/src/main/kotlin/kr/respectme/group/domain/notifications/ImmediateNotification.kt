package kr.respectme.group.domain.notifications

import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.utility.UUIDV7Generator
import java.time.Instant
import java.util.*

@DomainEntity
class ImmediateNotification(
    id: UUID = UUIDV7Generator.generate(),
    groupId: UUID = UUIDV7Generator.generate(),
    senderId: UUID = UUIDV7Generator.generate(),
    content: String = "",
    status: NotificationStatus = NotificationStatus.PENDING,
    createdAt: Instant = Instant.now(),
    updatedAt: Instant? = null,
    lastSentAt: Instant? = null,
): Notification(
    id = id,
    groupId = groupId,
    senderId = senderId,
    content = content,
    status = status,
    type = NotificationType.IMMEDIATE,
    createdAt = createdAt,
    updatedAt = updatedAt,
    lastSentAt = lastSentAt,
) {

    override fun validateType() {
        if(type != NotificationType.IMMEDIATE) {
            throw IllegalArgumentException("Notification type is not immediate")
        }
    }

    override fun toString(): String {
        return """
            ImmediateNotification
            id: $id,
            groupId: $groupId,
            senderId: $senderId,
            content: $content,
            status: $status,
            type: $type,
            createdAt: $createdAt,
            updatedAt: $updatedAt,
            lastSentAt: $lastSentAt 
        """.trimIndent()
    }
}