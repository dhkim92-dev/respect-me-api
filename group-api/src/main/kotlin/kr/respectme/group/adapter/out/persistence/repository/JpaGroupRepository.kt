package kr.respectme.group.adapter.out.persistence.repository

import kr.respectme.group.adapter.out.persistence.entity.JpaNotificationGroup
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JpaGroupRepository: org.springframework.data.repository.Repository<JpaNotificationGroup, UUID> {

    fun save(group: JpaNotificationGroup): JpaNotificationGroup

    fun findById(id: UUID): JpaNotificationGroup?

    fun deleteById(id: UUID)
}