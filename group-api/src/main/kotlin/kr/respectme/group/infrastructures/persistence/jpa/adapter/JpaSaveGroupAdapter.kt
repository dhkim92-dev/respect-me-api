package kr.respectme.group.infrastructures.persistence.jpa.adapter

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.respectme.group.domain.NotificationGroup
import kr.respectme.group.domain.mapper.GroupMapper
import kr.respectme.group.infrastructures.persistence.jpa.repository.JpaGroupRepository
import kr.respectme.group.infrastructures.persistence.port.SaveGroupPort
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Component
class JpaSaveGroupAdapter(
    @PersistenceContext
    private val em: EntityManager,
    private val groupMapper: GroupMapper,
    private val groupRepository: JpaGroupRepository,
) : SaveGroupPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun save(group: NotificationGroup): NotificationGroup {
        logger.debug("entity save")
        val jpaGroup = groupMapper.mapToJpaEntity(group)

        if(jpaGroup.members.isEmpty()) {
            groupRepository.delete(jpaGroup)
            return groupMapper.mapToDomainEntity(jpaGroup)
        }

        return groupMapper.mapToDomainEntity(groupRepository.save(jpaGroup))
    }

    @Transactional
    override fun deleteById(id: UUID) {
        groupRepository.deleteById(id)
    }

    override fun delete(group: NotificationGroup) {
        deleteById(group.id)
    }
}