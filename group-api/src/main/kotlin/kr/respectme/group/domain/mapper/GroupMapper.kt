package kr.respectme.group.domain.mapper

import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaGroupNotification
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup
import kr.respectme.group.infrastructures.persistence.jpa.repository.JpaGroupRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GroupMapper(
    private val groupRepository: JpaGroupRepository,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun mapToJpaEntity(domainGroup: NotificationGroup): JpaNotificationGroup {
        val jpaGroup = groupRepository.findById(domainGroup.id)?.apply {
            this.name = domainGroup.name
            this.description = domainGroup.description
            this.type = domainGroup.type
            this.ownerId = domainGroup.ownerId
            this.password= domainGroup.password
        } ?: createJpaGroup(domainGroup)

        remappingMembers(jpaGroup = jpaGroup, domainGroup = domainGroup)
        remappingNotifications(jpaGroup, domainGroup)

        return jpaGroup
    }

    fun mapToDomainEntity(jpaGroup: JpaNotificationGroup): NotificationGroup {
        logger.debug("jpaGroup loaded. notification size : ${jpaGroup.notifications.size}")
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

    /**
     * 도메인 그룹의 멤버와 JPA 그룹의 멤버를 비교하여 추가, 삭제, 업데이트를 수행한다.
     * @param jpaGroup JPA 그룹
     * @param domainGroup 도메인 그룹
     * @return List<JpaGroupMember> 최신 멤버 목록
     */
    private fun remappingMembers(jpaGroup: JpaNotificationGroup, domainGroup: NotificationGroup) {
        val newMembers = getNewMembers(jpaGroup, domainGroup)
        jpaGroup.members.addAll(newMembers)
        val removedMembers = getRemovedMembers(jpaGroup, domainGroup)
        jpaGroup.members.removeAll(removedMembers)
        updatedMembers(jpaGroup, domainGroup)
    }

    private fun getRemovedMembers(jpaGroup: JpaNotificationGroup, domainGroup: NotificationGroup)
    : Set<JpaGroupMember> {
        return jpaGroup.members.filter { jpaMember ->
            domainGroup.members.none { it.memberId == jpaMember.pk.memberId }
        }.toSet()
    }

    private fun getNewMembers(jpaGroup: JpaNotificationGroup, domainGroup: NotificationGroup)
    : Set<JpaGroupMember> {
        return domainGroup.members.filter { domainMember ->
            jpaGroup.members.none { it.pk.memberId == domainMember.memberId }
        }.map { GroupMemberMapper.mapToJpaEntity(it, jpaGroup) }.toSet()
    }

    private fun updatedMembers(jpaGroup: JpaNotificationGroup, domainGroup: NotificationGroup)
    : Set<JpaGroupMember> {
        return domainGroup.members.mapNotNull { domainMember ->
            jpaGroup.members.find { it.pk.memberId == domainMember.memberId }?.apply {
                this.nickname = domainMember.nickname
                this.memberRole = domainMember.memberRole
            }
        }.toSet()
    }

    private fun remappingNotifications(jpaGroup: JpaNotificationGroup, domainGroup: NotificationGroup) {
        val newNotifications = getAddedNotifications(jpaGroup, domainGroup)
        logger.debug("created notifications id : ${newNotifications.map { it.id }.joinToString { ", " }}")
        jpaGroup.notifications.addAll(newNotifications)
        val removedNotifications = getRemovedNotifications(jpaGroup, domainGroup)
        logger.debug("removed notifications size : ${removedNotifications.size}")
        removedNotifications.forEach{
            logger.debug("removed notification id : ${it.id}")
        }
        jpaGroup.notifications.removeAll(removedNotifications)
        updateNotifications(jpaGroup, domainGroup)
    }

    private fun updateNotifications(jpaGroup: JpaNotificationGroup, domainGroup: NotificationGroup) {
        domainGroup.notifications.mapNotNull { domainNotification ->
            jpaGroup.notifications.find { it.id == domainNotification.id }?.apply {
                NotificationMapper.mapToJpaNotification(domainNotification, jpaGroup)
            }
        }
    }

    private fun getRemovedNotifications(jpaGroup: JpaNotificationGroup, domainGroup: NotificationGroup)
    : Set<JpaGroupNotification> {
        logger.debug("Persisted Notification size : ${jpaGroup.notifications.size}")
        logger.debug("domain Notification size : ${domainGroup.notifications.size}")
        return jpaGroup.notifications.filter { jpaNotification ->
            domainGroup.notifications.none { it.id == jpaNotification.id }
        }.toSet()
    }

    private fun getAddedNotifications(jpaGroup: JpaNotificationGroup, domainGroup: NotificationGroup)
    : Set<JpaGroupNotification> {
        return domainGroup.notifications.filter { domainNotification ->
            jpaGroup.notifications.none { it.id == domainNotification.id }
        }.map { NotificationMapper.mapToJpaNotification(it, jpaGroup) }.toSet()
    }
}