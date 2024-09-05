package kr.respectme.group.infrastructures.persistence.jpa.entity.notifications

import jakarta.persistence.*
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.common.persistent.UUIDPkEntity
import kr.respectme.group.domain.notifications.NotificationStatus
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import kr.respectme.group.infrastructures.persistence.jpa.entity.converter.NotificationStateConverter
import kr.respectme.group.infrastructures.persistence.jpa.entity.converter.NotificationTypeConverter
import java.time.DayOfWeek
import java.time.Instant
import java.util.*

/**
 * Group Notification Jpa Entity
 * @property group JpaNotificationGroup
 * @property member JpaGroupMember
 * @property content contents of the notification
 * @property createdAt Notification created time
 * @property type type of notification (IMMEDIATELY, RESERVED)
 * @property status state of notification (CREATED, PUBLISHED, FAIL)
 */
@Entity(name = "group_notification")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.INTEGER)
class JpaGroupNotification(
    id: UUID = UUIDV7Generator.generate(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "group_id", referencedColumnName = "group_id", insertable = false, updatable = false),
        JoinColumn(name = "sender_id", referencedColumnName = "member_id", insertable = false, updatable = false)
    )
    val member: JpaGroupMember = JpaGroupMember(),
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false)
    val group: JpaNotificationGroup = JpaNotificationGroup(),
    content: String = "",
    type: NotificationType = NotificationType.IMMEDIATE,
    status : NotificationStatus = NotificationStatus.PENDING,
    lastSentAt: Instant? = null,
): UUIDPkEntity(id) {

    @Column
    var content: String = content
    @Convert(converter = NotificationTypeConverter::class)
    @Column(name = "type", insertable = false, updatable = false)
    var type: NotificationType = type
    @Convert(converter = NotificationStateConverter::class)
    var status: NotificationStatus = status
    @Column
    var lastSentAt : Instant? = lastSentAt
}