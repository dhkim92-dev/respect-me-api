package kr.respectme.group.infrastructures.persistence.jpa.entity.notifications

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import java.time.Instant
import java.time.LocalTime
import java.util.UUID

@Entity
@DiscriminatorValue("4")
class JpaDayIntervalNotification(
    id : UUID = UUIDV7Generator.generate(),
    group: JpaNotificationGroup = JpaNotificationGroup(),
    member: JpaGroupMember = JpaGroupMember(),
    status: NotificationStatus = NotificationStatus.PENDING,
    content: String = "",
    lastSentAt: Instant? = null,
    dayInterval: Int = 0,
    triggerTime: LocalTime = LocalTime.now()
) : JpaGroupNotification(
    id = id,
    group = group,
    member = member,
    content = content,
    type = NotificationType.REPEATED_INTERVAL,
    status = status,
    lastSentAt = lastSentAt,
) {
    @Column
    var dayInterval: Int = dayInterval
    @Column
    var triggerTime: LocalTime = triggerTime
}