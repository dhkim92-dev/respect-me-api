package kr.respectme.group.adapter.out.persistence

import kr.respectme.common.domain.cache.DomainEntityCache
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupNotificationRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.mapper.GroupMapper
import kr.respectme.group.domain.notifications.ImmediateNotification
import kr.respectme.group.domain.notifications.Notification
import kr.respectme.group.domain.notifications.NotificationType
import kr.respectme.group.domain.notifications.ScheduledNotification
import kr.respectme.group.port.out.persistence.LoadGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class JpaLoadGroupAdapter(
    private val entityCache: DomainEntityCache,
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository,
    private val groupNotificationRepository: JpaGroupNotificationRepository,
    private val groupMapper: GroupMapper,
): LoadGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadGroup(groupId: UUID, memberIds: List<UUID>, notificationIds: List<UUID>): NotificationGroup? {
        val group = groupRepository.findById(groupId)
        if (group == null) {
            logger.warn("Group not found: $groupId")
            return null
        }

//        val searchMembers =  listOf(group.ownerId) + memberIds
        val members = groupMemberRepository.findByMemberIdInAndGroupId(memberIds, groupId)

        val notifications = if(notificationIds.isEmpty()) {
            emptyList()
        } else {
            groupNotificationRepository.findByIdInAndGroupId(notificationIds, groupId)
        }

        val domainGroup = groupMapper.toDomain(group, members, notifications)
        entityCache.put(NotificationGroup::class.java, domainGroup)
        cacheGroupMembers(domainGroup.getMembers().toList())
        cacheNotifications(domainGroup.getNotifications().toList())

        logger.debug("domain group converted")

        return domainGroup
    }

    override fun loadGroupMemberIds(groupId: UUID): List<UUID> {
        return groupMemberRepository.findByGroupId(groupId)
            .map { it.memberId }
    }

    private fun cacheGroupMembers(members: List<GroupMember>) {
        members.forEach { member ->
            entityCache.put(GroupMember::class.java, member)
        }
    }

    private fun cacheNotifications(notifications: List<Notification>) {
        notifications.forEach { notification ->
            when(notification.getType()) {
                NotificationType.IMMEDIATE ->
                    entityCache.put(ImmediateNotification::class.java, notification as ImmediateNotification)
                NotificationType.SCHEDULED ->
                    entityCache.put(ScheduledNotification::class.java, notification as ScheduledNotification)
                else -> throw IllegalArgumentException("Unknown notification type: ${notification.getType()}")
            }
        }
    }
}