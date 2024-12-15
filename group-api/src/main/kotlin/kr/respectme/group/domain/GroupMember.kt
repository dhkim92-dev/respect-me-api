package kr.respectme.group.domain

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import java.time.Instant
import java.util.*

/**
 * GroupMember Domain Entity
 * group member can leave group or if the member is owner or admin, can kick member.
 * can change member etc
 */

class GroupMemberId(
    val groupId: UUID,
    val memberId: UUID
){
    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other !is GroupMemberId) return false
        if(this === other) return true
        return this.groupId == other.groupId && this.memberId == other.memberId
    }

    override fun hashCode(): Int {
        return Objects.hash(groupId, memberId)
    }
}

@DomainEntity
class GroupMember(
    memberId: UUID = UUID.randomUUID(),
    groupId: UUID = UUID.randomUUID(),
    nickname: String = "",
    memberRole: GroupMemberRole = GroupMemberRole.MEMBER,
    profileImageUrl : String? = null,
    createdAt: Instant = Instant.now()
): BaseDomainEntity<GroupMemberId>(GroupMemberId(groupId, memberId)) {

    val memberId: UUID
        get() = id.memberId

    val groupId: UUID
        get() = id.groupId

    var nickname: String = nickname
        private set

    var memberRole: GroupMemberRole = memberRole
        private set

    var profileImageUrl: String? = profileImageUrl
        private set

    val createdAt = createdAt

    fun isGroupOwner(): Boolean {
        return memberRole == GroupMemberRole.OWNER
    }

    fun isGroupAdmin(): Boolean {
        return memberRole == GroupMemberRole.ADMIN
    }

    fun isSameMember(memberId: UUID): Boolean {
        return this.memberId == memberId
    }

    fun isGroupMember(): Boolean {
        return memberRole == GroupMemberRole.MEMBER
    }

    fun changeMemberRole(role: GroupMemberRole?) {
        role?.let{
            this.memberRole = role
        }
    }

    fun changeNickname(nickname: String?) {
        nickname?.let{
            this.nickname = nickname
        }
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false
        other as GroupMember
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}