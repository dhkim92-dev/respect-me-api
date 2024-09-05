package kr.respectme.group.infrastructures.persistence.jpa.entity.notifications

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.domain.notifications.DayOfWeek
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import java.time.Instant
import java.time.LocalTime
import java.util.UUID

@Entity
@DiscriminatorValue("3")
class JpaWeeklyNotification(
    id: UUID = UUIDV7Generator.generate(),
    group: JpaNotificationGroup = JpaNotificationGroup(),
    member: JpaGroupMember = JpaGroupMember(),
    content: String = "",
    status: NotificationStatus = NotificationStatus.PENDING,
    dayOfWeeks: Int = 0,
    triggerTime : LocalTime = LocalTime.now()
): JpaGroupNotification(
    id = id,
    group = group,
    member = member,
    content = content,
    status = status,
    type = NotificationType.REPEATED_WEEKLY,
) {

    @Column
    var dayOfWeeks: Int = dayOfWeeks

    @Column
    var triggerTime: LocalTime = triggerTime
}