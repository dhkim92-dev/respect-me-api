package kr.respectme.group.adapter.out.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.domain.cache.DomainEntityCache
import kr.respectme.common.domain.enums.EntityStatus
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.BusinessException
import kr.respectme.common.error.InternalServerError
import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaGroupNotification
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupNotificationRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.domain.GroupMember
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.mapper.GroupMapper
import kr.respectme.group.domain.mapper.GroupMemberMapper
import kr.respectme.group.domain.mapper.NotificationMapper
import kr.respectme.group.domain.notifications.*
import kr.respectme.group.port.out.persistence.SaveGroupPort
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.util.*
import javax.swing.text.html.parser.Entity


@Component
class JpaSaveGroupAdapter(
    private val entityCache: DomainEntityCache,
    private val groupMapper: GroupMapper,
    private val memberMapper: GroupMemberMapper,
    private val notificationMapper: NotificationMapper,
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository,
    private val groupNotificationRepository: JpaGroupNotificationRepository
) : SaveGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun save(group: NotificationGroup): NotificationGroup {
        val groupEntity = groupMapper.toEntity(group)
        val members = mutableListOf<JpaGroupMember>()
        val notifications = mutableListOf<JpaGroupNotification>()

        handleGroup(group, groupEntity)
        members.addAll(handleMembers(group))
        notifications.addAll(handleNotifications(group))

        return groupMapper.toDomain(groupEntity, members, notifications)
    }

    private fun handleGroup(group: NotificationGroup, groupEntity: JpaNotificationGroup) {
        logger.debug("handleGroup called.")
        if(entityCache.contains(NotificationGroup::class.java, group)) {
            if(entityCache.isSameWithCache(NotificationGroup::class.java, group) == EntityStatus.UPDATED) {
                logger.debug("group ${group.id} is cached, and changed.")
                groupRepository.save(groupEntity) // 변경사항이 있을때만 save 호출
            } else {
                logger.debug("group ${group.id} is cached, and not changed.")
            }
        } else {
            logger.debug("new group created.")
            groupRepository.save(groupEntity)
        }
    }

    private fun handleMembers(group: NotificationGroup): List<JpaGroupMember> {
        val deletedMembers = getDeletedMembers(group)
        val updatedMembers = getUpdatedMembers(group)
        val newMembers = getNewMembers(group)
        val activeMembers = mutableListOf<JpaGroupMember>()
        logger.debug("deletedMembers: ${deletedMembers.size}")

        updatedMembers.union(newMembers).forEach { member ->
            groupMemberRepository.save(memberMapper.toEntity(member))
            entityCache.put(GroupMember::class.java, member)
            activeMembers.add(memberMapper.toEntity(member))
        }

        deletedMembers.forEach { member ->
            groupMemberRepository.deleteById(member.id)
            entityCache.evict(GroupMember::class.java, member)
        }

        return activeMembers.toList()
    }

    private fun handleNotifications(group: NotificationGroup): List<JpaGroupNotification> {
        val deletedNotifications = getDeletedNotifications(group)
        val updatedNotifications = getUpdatedNotifications(group)
        val newNotifications = getNewNotifications(group)
        val activeNotifications = mutableListOf<JpaGroupNotification>()

        updatedNotifications.union(newNotifications).forEach { notification ->
            groupNotificationRepository.save(notificationMapper.toEntity(notification))
            when(notification.getType()) {
                NotificationType.IMMEDIATE -> entityCache.put(ImmediateNotification::class.java, notification as ImmediateNotification)
                NotificationType.SCHEDULED -> entityCache.put(ScheduledNotification::class.java, notification as ScheduledNotification)
                else -> throw InternalServerError("Unknown notification type")
            }
            activeNotifications.add(notificationMapper.toEntity(notification))
        }

        deletedNotifications.forEach { notification ->
            groupNotificationRepository.deleteById(notification.id)
            when(notification.getType()) {
                NotificationType.IMMEDIATE -> entityCache.evict(ImmediateNotification::class.java, notification)
                NotificationType.SCHEDULED -> entityCache.evict(ScheduledNotification::class.java, notification)
                else -> throw InternalServerError("Unknown notification type")
            }
        }

        return activeNotifications.toList()
    }

    private fun <T: Notification> checkNotificationExistsInCache(entity: T): Boolean {
        return when(entity.getType()) {
            NotificationType.IMMEDIATE -> entityCache.contains(ImmediateNotification::class.java, entity.id)
            NotificationType.SCHEDULED -> entityCache.contains(ScheduledNotification::class.java, entity.id)
            else -> false
        }
    }

    private fun <T: Notification> checkNotificationUpdated(entity: T): EntityStatus{
        return when(entity.getType()) {
            NotificationType.IMMEDIATE -> entityCache.isSameWithCache(ImmediateNotification::class.java, entity as ImmediateNotification)
            NotificationType.SCHEDULED -> entityCache.isSameWithCache(ScheduledNotification::class.java, entity as ScheduledNotification)
            else -> throw InternalServerError("Unknown notification type")
        }
    }

    private fun getDeletedNotifications(group: NotificationGroup): List<Notification> {
        return entityCache.get(NotificationGroup::class.java, group)?.let { cachedGroup ->
            // 현재 그룹 캐시가 존재하는 경우에만 이 로직이 유효하다.
            cachedGroup.getNotifications().filter { notification -> group.getNotifications().none { it.id == notification.id } }
        } ?: emptyList()
    }

    private fun getUpdatedNotifications(group: NotificationGroup): List<Notification> {
        // 현재 노티피케이션 중 캐시에 존재하고, 내용이 변한 경우
        return group.getNotifications().filter { notification -> checkNotificationUpdated(notification) == EntityStatus.UPDATED }
    }

    private fun getNewNotifications(group: NotificationGroup): List<Notification> {
        return group.getNotifications().filter { notification -> !checkNotificationExistsInCache(notification) }
    }

    private fun getDeletedMembers(group: NotificationGroup): List<GroupMember> {
        return entityCache.get(NotificationGroup::class.java, group)?.let { cachedGroup ->
            cachedGroup.getMembers().filter { member -> group.getMembers().none { it.id == member.id } }
        } ?: emptyList()
    }

    private fun getUpdatedMembers(group: NotificationGroup): List<GroupMember> {
        return entityCache.get(NotificationGroup::class.java, group)?.let { cachedGroup ->
            cachedGroup.getMembers().filter { member -> group.getMembers().none { it.id == member.id } }
        } ?: emptyList()
    }

    private fun getNewMembers(group: NotificationGroup): List<GroupMember> {
        return group.getMembers().filter { member -> !entityCache.contains(GroupMember::class.java, member) }
    }

    override fun deleteById(id: UUID) {
        groupRepository.deleteById(id)
    }

    override fun delete(group: NotificationGroup) {
        deleteById(group.id)
    }
}