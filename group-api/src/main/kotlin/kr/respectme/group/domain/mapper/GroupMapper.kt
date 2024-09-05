package kr.respectme.group.domain.mapper

import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.notifications.ImmediateNotification
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.domain.notifications.ScheduledNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaGroupNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaScheduledNotification
import kr.respectme.group.infrastructures.persistence.jpa.repository.JpaGroupRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GroupMapper(
    private val groupRepository: JpaGroupRepository,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun mapToJpaEntity(domainGroup: NotificationGroup): JpaNotificationGroup {
        val group = groupRepository.findById(domainGroup.id)?.apply {
            this.name = domainGroup.name
            this.description = domainGroup.description
            this.type = domainGroup.type
            this.ownerId = domainGroup.ownerId
            this.password= domainGroup.password
        } ?: createJpaGroup(domainGroup)

        domainGroup.members.forEach { groupMember ->
            group.members.add(GroupMemberMapper.mapToJpaEntity(groupMember, group))
        }

        domainGroup.notifications.forEach { notification ->
            group.notifications.add(NotificationMapper.mapToJpaNotification(notification, group))
        }

        return group
    }

    fun mapToDomainEntity(jpaGroup: JpaNotificationGroup): NotificationGroup {
        return NotificationGroup(
            id = jpaGroup.id,
            ownerId = jpaGroup.ownerId,
            name = jpaGroup.name,
            type = jpaGroup.type,
            description = jpaGroup.description,
            password = jpaGroup.password,
            members = jpaGroup.members.map {it ->
                GroupMemberMapper.mapToDomain(it)
            }.toMutableSet(),
            notifications = jpaGroup.notifications.map {jpaNotification ->
                NotificationMapper.mapToDomainEntity(jpaNotification)
            }.toMutableSet()
        )
    }

    private fun createJpaGroup(domainGroup: NotificationGroup): JpaNotificationGroup {
        return JpaNotificationGroup(
            ownerId = domainGroup.ownerId,
            name = domainGroup.name,
            type = domainGroup.type,
            description = domainGroup.description,
            password = domainGroup.password,
        )
    }
}