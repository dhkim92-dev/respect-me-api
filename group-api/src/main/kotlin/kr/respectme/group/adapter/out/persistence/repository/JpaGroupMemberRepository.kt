package kr.respectme.group.adapter.out.persistence.repository

import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface JpaGroupMemberRepository: JpaRepository<JpaGroupMember, JpaGroupMember.Pk> {

    fun findByPkMemberIdInAndPkGroupId(memberIds: List<UUID>, groupId: UUID): List<JpaGroupMember>

    fun findPkMemberIdByPkGroupId(groupId: UUID): List<JpaGroupMember>
}