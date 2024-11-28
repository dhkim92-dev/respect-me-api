package kr.respectme.group.domain.mapper

import kr.respectme.group.domain.GroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaGroupMember
import kr.respectme.group.infrastructures.persistence.jpa.entity.JpaNotificationGroup

object GroupMemberMapper {

    fun mapToDomain(jpaGroupMember: JpaGroupMember): GroupMember {
        return GroupMember(
            memberId = jpaGroupMember.pk.memberId,
            groupId = jpaGroupMember.pk.groupId,
            nickname = jpaGroupMember.nickname,
            memberRole = jpaGroupMember.memberRole,
            createdAt = jpaGroupMember.createdAt
        )
    }

    fun mapToJpaEntity(groupMember: GroupMember, jpaGroup: JpaNotificationGroup): JpaGroupMember {
        val pk = JpaGroupMember.Pk(
            memberId = groupMember.memberId,
            groupId = groupMember.groupId
        )

        return jpaGroup.members.find { it.pk == pk }?.apply {
            this.nickname = groupMember.nickname
            this.memberRole = groupMember.memberRole
        } ?: JpaGroupMember(
            pk = pk,
            group = jpaGroup,
            nickname = groupMember.nickname,
            memberRole = groupMember.memberRole,
        )
    }
}