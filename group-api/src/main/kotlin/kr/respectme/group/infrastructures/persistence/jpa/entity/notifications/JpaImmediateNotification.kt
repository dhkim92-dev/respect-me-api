package kr.respectme.group.infrastructures.persistence.jpa.entity.notifications

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import java.time.Instant
import java.util.*

@Entity
@DiscriminatorValue("1")
class JpaImmediateNotification(
    id: UUID = UUIDV7Generator.generate(),
    group: JpaNotificationGroup = JpaNotificationGroup(),
    member: JpaGroupMember = JpaGroupMember(),
    status: NotificationStatus = NotificationStatus.PENDING,
    content: String = "",
    lastSentAt: Instant? = null
) : JpaGroupNotification(
    id = id,
    group = group,
    member = member,
    type = NotificationType.IMMEDIATE,
    status = status,
    lastSentAt = lastSentAt,
    content = content,
) {

}