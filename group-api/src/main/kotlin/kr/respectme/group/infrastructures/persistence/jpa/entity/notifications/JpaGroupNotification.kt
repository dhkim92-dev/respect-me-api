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
import org.slf4j.LoggerFactory
import java.time.DayOfWeek
import java.time.Instant
import java.util.*
import kotlin.jvm.Transient

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
    member: JpaGroupMember = JpaGroupMember(),
    group: JpaNotificationGroup = JpaNotificationGroup(),
    content: String = "",
    type: NotificationType = NotificationType.IMMEDIATE,
    status : NotificationStatus = NotificationStatus.PENDING,
    lastSentAt: Instant? = null,
): UUIDPkEntity(id) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "group_id", referencedColumnName = "group_id", insertable = true, updatable = true),
        JoinColumn(name = "sender_id", referencedColumnName = "member_id", insertable = true, updatable = true)
    )
    var member = member

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false)
    var group: JpaNotificationGroup = group

    @Column
    var content: String = content
    @Convert(converter = NotificationTypeConverter::class)
    @Column(name = "type", insertable = false, updatable = false)
    var type: NotificationType = type
    @Convert(converter = NotificationStateConverter::class)
    var status: NotificationStatus = status
    @Column
    var lastSentAt : Instant? = lastSentAt

    @Transient
    private val logger = LoggerFactory.getLogger(javaClass)

    @PrePersist
    fun prePersist() {
        logger.debug("prepersist : {}", toString())
    }

    override fun toString(): String {
        return "entity : " +
                "id: $id, " +
                "member: ${member.pk.memberId}, " +
                "member group : ${member.pk.groupId}" +
                "member nickname : ${member.nickname}" +
                "group: ${group.id}, " +
                "content: $content, " +
                "type: $type, " +
                "status: $status, " +
                "lastSentAt: $lastSentAt"
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(this === other) return true
        if(javaClass != other.javaClass) return false
        other as JpaGroupNotification
        return id == other.id
    }
}