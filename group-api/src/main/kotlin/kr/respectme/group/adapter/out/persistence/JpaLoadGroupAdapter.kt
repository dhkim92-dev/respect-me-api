package kr.respectme.group.adapter.out.persistence

import kr.respectme.group.adapter.out.persistence.repository.JpaGroupMemberRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupNotificationRepository
import kr.respectme.group.adapter.out.persistence.repository.JpaGroupRepository
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.mapper.GroupMapper
import kr.respectme.group.domain.mapper.GroupMemberMapper
import kr.respectme.group.domain.mapper.NotificationMapper
import kr.respectme.group.port.out.persistence.LoadGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class JpaLoadGroupAdapter(
    private val groupRepository: JpaGroupRepository,
    private val groupMemberRepository: JpaGroupMemberRepository,
    private val groupNotificationRepository: JpaGroupNotificationRepository,
    private val groupMapper: GroupMapper,
    private val memberMapper: GroupMemberMapper,
    private val notificationMapper: NotificationMapper
): LoadGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadGroup(groupId: UUID, memberIds: List<UUID>, notificationIds: List<UUID>): NotificationGroup? {
        val group = groupRepository.findById(groupId)
        if (group == null) {
            logger.warn("Group not found: $groupId")
            return null
        }

        val members = if(memberIds.isEmpty()) {
            emptyList()
        } else {
            groupMemberRepository.findByPkMemberIdInAndPkGroupId(memberIds, groupId)
        }

        val notifications = if(notificationIds.isEmpty()) {
            emptyList()
        } else {
            groupNotificationRepository.findByIdInAndGroupId(notificationIds, groupId)
        }

        return groupMapper.toDomain(group, members, notifications)
    }
}