package kr.respectme.group.adapter.out.persistence

import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import kr.respectme.group.adapter.out.persistence.entity.notifications.JpaGroupNotification
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupNotificationRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.domain.EntityStatus
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.mapper.GroupMapper
import kr.respectme.group.domain.mapper.GroupMemberMapper
import kr.respectme.group.domain.mapper.NotificationMapper
import kr.respectme.group.port.out.persistence.SaveGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*


@Component
class JpaSaveGroupAdapter(
    private val groupMapper: GroupMapper,
    private val memberMapper: GroupMemberMapper,
    private val notificationMapper: NotificationMapper,
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository,
    private val groupNotificationRepository: JpaGroupNotificationRepository
) : SaveGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun save(group: NotificationGroup): NotificationGroup {
        var groupEntity = groupMapper.toEntity(group)
        logger.info("domain entity status : ${group.entityStatus}")
        logger.info("group entity isNew?  ${groupEntity.isNew}")
        if(group.isNew() || group.isUpdated()) {
            logger.info("Group is new or updated, saving group")
            groupEntity = groupRepository.save(groupEntity)
            logger.info("Group saved : ${groupEntity.id}")
        }

        val members = handleMembers(group)
        val notifications = handleNotifications(group)

        return groupMapper.toDomain(groupEntity, members, notifications)
    }

    private fun handleNotifications(group: NotificationGroup): List<JpaGroupNotification> {
        val updatedNotifications = group.notifications.filter { it.isUpdated() }
        val newNotifications = group.notifications.filter { it.isNew() }
        val deletedNotifications = group.notifications.filter { it.isRemoved() }
        val activeNotifications: MutableList<JpaGroupNotification> = mutableListOf()

        // 삭제 먼저 수행
        deletedNotifications.forEach {
            groupNotificationRepository.deleteById(it.id)
        }

        // 업데이트 수행
        updatedNotifications.forEach {
            val notification = notificationMapper.toEntity(it)
            activeNotifications.add(groupNotificationRepository.save(notification))
        }

        // 신규생성
        newNotifications.forEach {
            val notification = notificationMapper.toEntity(it)
            groupNotificationRepository.save(notification)
            activeNotifications.add(groupNotificationRepository.save(notification))
        }

        return activeNotifications
    }

    private fun handleMembers(group: NotificationGroup)
    : List<JpaGroupMember> {
        val updatedMembers = group.members.filter { it.isUpdated() }
        val newMembers = group.members.filter { it.isNew() }
        val deletedMembers = group.members.filter { it.isRemoved() }
        val activeMembers: MutableList<JpaGroupMember> = mutableListOf()

        // 삭제 먼저 수행
        deletedMembers.forEach {
            groupMemberRepository.deleteById(JpaGroupMember.Pk(groupId = it.groupId, memberId = it.memberId))
        }
        // 업데이트 수행
        activeMembers.addAll(updatedMembers.map { groupMemberRepository.save(memberMapper.toEntity(it)) })
        // 신규생성
        activeMembers.addAll(newMembers.map { groupMemberRepository.save(memberMapper.toEntity(it)) })

        return activeMembers
    }

    override fun deleteById(id: UUID) {
        groupRepository.deleteById(id)
    }

    override fun delete(group: NotificationGroup) {
        deleteById(group.id)
    }
}