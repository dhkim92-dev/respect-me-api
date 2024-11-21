package kr.respectme.group.domain.mapper

import kr.respectme.group.application.dto.notification.NotificationDto
import kr.respectme.group.domain.notifications.ImmediateNotification
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.domain.notifications.ScheduledNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaGroupNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaImmediateNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaScheduledNotification
import org.slf4j.LoggerFactory

/**
 * Mapper for notification entity
 */
object NotificationMapper {


    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Maps a JPA notification to a domain notification.
     * @param jpaNotification: The JPA notification to map.
     * @return Notification
     * @throws IllegalArgumentException if the notification type is not valid.
     */
    fun mapToDomainEntity(jpaNotification: JpaGroupNotification): Notification {
        return when(jpaNotification) {
            is JpaImmediateNotification -> castToImmediateNotification(jpaNotification)
            is JpaScheduledNotification -> castToScheduledNotification(jpaNotification)
            else -> throw IllegalArgumentException("Notification type is not valid")
        }
    }

    /**
     * Maps a domain notification to a JPA notification.
     * @param notification: The domain notification to map.
     * @param jpaNotificationGroup: The JPA notification group to map to.
     */
    fun mapToJpaNotification(
        notification: Notification,
        jpaNotificationGroup: JpaNotificationGroup
    ): JpaGroupNotification {
        val jpaNotification = jpaNotificationGroup.notifications.find{ it.id == notification.id }
            ?.apply{
                logger.debug("notification modified : ${notification.id}")
                this.type = notification.type
                this.status = notification.status
                this.content = notification.content
                this.createdAt = notification.createdAt
                this.updatedAt = notification.updatedAt
                this.lastSentAt = notification.lastSentAt
                castToJpaEntity(notification, this)
            }
            ?: createJpaNotification(notification, jpaNotificationGroup)

        logger.debug("notification modified : ${jpaNotification.toString()}")
        return jpaNotification
    }

    /**
     * create new JpaGroupNotification entity
     * @param notification: The domain notification to map.
     */
    private fun createJpaNotification(notification: Notification, jpaGroup: JpaNotificationGroup): JpaGroupNotification {
        logger.debug("new notification created : ${notification.id}")
        return when (notification) {
            is ImmediateNotification -> createJpaImmediateNotification(notification, jpaGroup)
            is ScheduledNotification -> createJpaScheduledNotification(notification, jpaGroup)
            else -> throw IllegalArgumentException("Notification type is not valid")
        }
    }

    /**
     * create new JpaImmediateNotification entity
     * @param notification: The domain notification to map.
     * @param jpaGroup: The JPA notification group entity, only use for search group member
     */
    private fun createJpaImmediateNotification(notification: ImmediateNotification, jpaGroup: JpaNotificationGroup): JpaImmediateNotification {
        val entity = JpaImmediateNotification(
            id = notification.id,
            group = jpaGroup,
            member = jpaGroup.members.find { it.pk.memberId == notification.senderId }!!,
            content = notification.content,
            status = notification.status,
            lastSentAt = notification.lastSentAt,
        )
        logger.debug("""createJpaImmediateNotification
            |notificationId: ${entity.id}
            |groupId: ${entity.member.group.id}
            |memberId: ${entity.member.pk.memberId}
            |content: ${entity.content}
            |type: ${entity.type}
            |state: ${entity.status}
            |scheduledAt: null
            |dayOfWeeks: null
            |dayInterval: null
            |createdAt: ${entity.createdAt}
            |updatedAt: ${entity.updatedAt}
            |lastSentAt: ${entity.lastSentAt}
        """.trimMargin())
        return entity
    }

    /**
     * create new JpaScheduledNotification entity
     * @param notification: The domain notification to map.
     * @param jpaGroup: The JPA notification group entity, only use for search group member
     */
    private fun createJpaScheduledNotification(notification: ScheduledNotification, jpaGroup: JpaNotificationGroup): JpaScheduledNotification {
        return JpaScheduledNotification(
            id = notification.id,
            group = jpaGroup,
            member = jpaGroup.members.find { it.pk.memberId == notification.senderId }!!,
            content = notification.content,
            status = notification.status,
            lastSentAt = notification.lastSentAt,
            scheduledAt = notification.scheduledAt,
        )
    }

    /**
     * map domain notification entity to JPA notification entity
     * @param notification: The domain notification to map.
     * @param jpaNotification: The JPA notification to map to.
     */
    private fun castToJpaEntity(notification: Notification, jpaNotification: JpaGroupNotification): JpaGroupNotification {
        val jpaEntity = when(notification) {
            is ImmediateNotification -> castToJpaImmediateNotification(notification, jpaNotification)
            is ScheduledNotification -> castToJpaScheduledNotification(notification, jpaNotification)
            else -> throw IllegalArgumentException("Notification type is not valid")
        }

        logger.debug("jpa notification entity : ${jpaEntity}")

        return jpaEntity
    }

    /**
     * map domain ImmediateNotification entity to JPA ImmediateNotification entity
     * @param notification: The domain ImmediateNotification to map.
     * @param jpaNotification: The JPA ImmediateNotification to map to.
     * @return JpaImmediateNotification
     */
    private fun castToJpaImmediateNotification(
        notification: ImmediateNotification,
        jpaNotification: JpaGroupNotification
    ): JpaImmediateNotification {
        return (jpaNotification as JpaImmediateNotification).apply{
            this.content = notification.content
            this.lastSentAt = notification.lastSentAt
        }
    }

    /**
     * map domain ScheduledNotification entity to JPA ScheduledNotification entity
     * @param notification: The domain ScheduledNotification to map.
     * @param jpaNotification: The JPA ScheduledNotification to map to.
     * @return JpaScheduledNotification
     */
    private fun castToJpaScheduledNotification(
        notification: ScheduledNotification,
        jpaNotification: JpaGroupNotification
    ): JpaScheduledNotification {
        return (jpaNotification as JpaScheduledNotification).apply{
            this.content = notification.content
            this.lastSentAt = notification.lastSentAt
            this.scheduledAt = notification.scheduledAt
        }
    }

    /**
     * map JPA ImmediateNotification entity to domain ImmediateNotification entity
     * @param jpaNotification: The JPA ImmediateNotification to map.
     * @return ImmediateNotification
     */
    private fun castToImmediateNotification(jpaNotification: JpaImmediateNotification): ImmediateNotification {
        return ImmediateNotification(
            id = jpaNotification.id,
            groupId = jpaNotification.member.group.id,
            senderId = jpaNotification.member.pk.memberId,
            content = jpaNotification.content,
            status = jpaNotification.status,
            createdAt = jpaNotification.createdAt,
            updatedAt = jpaNotification.updatedAt,
            lastSentAt = jpaNotification.lastSentAt,
        )
    }

    /**
     * map JPA ScheduledNotification entity to domain ScheduledNotification entity
     * @param jpaNotification: The JPA ScheduledNotification to map.
     * @return ScheduledNotification
     */
    private fun castToScheduledNotification(jpaNotification: JpaScheduledNotification): ScheduledNotification {
        return ScheduledNotification(
            id = jpaNotification.id,
            groupId = jpaNotification.member.group.id,
            senderId = jpaNotification.member.pk.memberId,
            content = jpaNotification.content,
            status = jpaNotification.status,
            createdAt = jpaNotification.createdAt,
            updatedAt = jpaNotification.updatedAt,
            lastSentAt = jpaNotification.lastSentAt,
            scheduledAt = jpaNotification.scheduledAt,
        )
    }

    /**
     * map JPA ImmediateNotification entity to NotificationDto
     */
    fun mapToNotificationDto(jpaNotification: JpaImmediateNotification): NotificationDto {
        logger.debug("mapToNotificationDto(immediate) called.")
        logger.debug("""mapToNotificationDto(ImmediateNotification) 
            |notificationId: ${jpaNotification.id}
            |groupId: ${jpaNotification.group.id}
            |content: ${jpaNotification.content}
            |type: ${jpaNotification.type}
            |state: ${jpaNotification.status}
            |scheduledAt: null
            |dayOfWeeks: null
            |dayInterval: null
            |createdAt: ${jpaNotification.createdAt}
            |updatedAt: ${jpaNotification.updatedAt}
            |lastSentAt: ${jpaNotification.lastSentAt}
        """.trimMargin())
        return NotificationDto(
            notificationId = jpaNotification.id,
            groupId = jpaNotification.group.id,
            content = jpaNotification.content,
            type = jpaNotification.type,
            state = jpaNotification.status,
            scheduledAt = null,
            dayOfWeeks = null,
            dayInterval = null,
            createdAt = jpaNotification.createdAt,
            updatedAt = jpaNotification.updatedAt,
            lastSentAt = jpaNotification.lastSentAt,
        )
    }

    /**
     * map JPA ScheduledNotification entity to NotificationDto
     */
    fun mapToNotificationDto(jpaNotification: JpaScheduledNotification): NotificationDto {
        logger.debug("mapToNotificationDto(scheduled) called.")
        logger.debug("""mapToNotificationDto(JpaScheduledNotification)
            |notificationId: ${jpaNotification.id}
            |groupId: ${jpaNotification.group.id}
            |content: ${jpaNotification.content}
            |type: ${jpaNotification.type}
            |state: ${jpaNotification.status}
            |scheduledAt: ${jpaNotification.scheduledAt}
            |createdAt: ${jpaNotification.createdAt}
            |updatedAt: ${jpaNotification.updatedAt}
            |lastSentAt: ${jpaNotification.lastSentAt}
            |scheduledAt: ${jpaNotification.scheduledAt}
            |dayOfWeeks: null
            |dayInterval: null
            |createdAt: ${jpaNotification.createdAt}
            |updatedAt: ${jpaNotification.updatedAt}
            |lastSentAt: ${jpaNotification.lastSentAt}
        """.trimMargin())
        return NotificationDto(
            notificationId = jpaNotification.id,
            groupId = jpaNotification.group.id,
            content = jpaNotification.content,
            type = jpaNotification.type,
            state = jpaNotification.status,
            scheduledAt = jpaNotification.scheduledAt,
            createdAt = jpaNotification.createdAt,
            updatedAt = jpaNotification.updatedAt,
            lastSentAt = jpaNotification.lastSentAt,
            dayInterval = null,
            dayOfWeeks = null,
        )
    }
}