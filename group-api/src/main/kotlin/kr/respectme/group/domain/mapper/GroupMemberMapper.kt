package kr.respectme.group.domain.mapper

import kr.respectme.group.adapter.out.persistence.entity.JpaGroupMember
import kr.respectme.group.domain.GroupMember
import org.springframework.stereotype.Component

@Component
class GroupMemberMapper {

    fun toDomain(member: JpaGroupMember): GroupMember {
        val groupMember = GroupMember(
            memberId = member.pk!!.memberId,
            groupId = member.pk!!.groupId,
            nickname = member.nickname,
            memberRole = member.memberRole,
            createdAt = member.createdAt,
            profileImageUrl = member.profileImageUrl
        )
        groupMember.loaded()
        return groupMember
    }

    fun toEntity(member: GroupMember): JpaGroupMember {
        return JpaGroupMember(
            pk = JpaGroupMember.Pk(
                memberId = member.memberId,
                groupId = member.groupId
            ),
            nickname = member.nickname,
            memberRole = member.memberRole,
            profileImageUrl = member.profileImageUrl
        )
    }
}