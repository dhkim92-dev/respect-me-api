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
@DiscriminatorValue("2")
class JpaScheduledNotification(
    id: UUID = UUIDV7Generator.generate(),
    member: JpaGroupMember = JpaGroupMember(),
    group: JpaNotificationGroup = JpaNotificationGroup(),
    content: String = "",
    status: NotificationStatus = NotificationStatus.PENDING,
    lastSentAt: Instant? = null,
    scheduledAt : Instant = Instant.now(),
): JpaGroupNotification(id, member, group, content, NotificationType.SCHEDULED, status, lastSentAt) {

    var scheduledAt: Instant = scheduledAt
}