package kr.respectme.group.infrastructures.persistence.jpa.adapter

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.mapper.GroupMapper
import kr.respectme.group.infrastructures.persistence.jpa.repository.JpaGroupRepository
import kr.respectme.group.infrastructures.persistence.port.LoadGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class JpaLoadGroupAdapter(
    @PersistenceContext private val em: EntityManager,
    private val groupRepository: JpaGroupRepository,
    private val groupMapper: GroupMapper
): LoadGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadGroup(groupId: UUID): NotificationGroup? {
        // 일단은 fetch join을 통해서 모든 데이터를 가져온다
        // 최적화는 나중에 생각하자
        val group = groupRepository.findById(groupId) // 프록시 객체로 로딩
        groupRepository.findByIdWithMembers(groupId) // members join fetch
        groupRepository.findByIdWithNotifications(groupId) // notifications join fetch
//        group?.members?.forEach { logger.debug("member nickname : ${it.nickname} role : ${it.memberRole}") }
        return group?.let { groupMapper.mapToDomainEntity(group) }
    }
}