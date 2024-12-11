package kr.respectme.group.domain

import java.time.Instant
import java.util.*

/**
 * GroupMember Domain Entity
 * group member can leave group or if the member is owner or admin, can kick member.
 * can change member etc
 */
class GroupMember(
    val memberId: UUID = UUID.randomUUID(),
    val groupId: UUID = UUID.randomUUID(),
    nickname: String = "",
    memberRole: GroupMemberRole = GroupMemberRole.MEMBER,
    profileImageUrl : String? = null,
    val createdAt: Instant = Instant.now()
): BaseDomainEntity() {

    var nickname: String = nickname
        private set

    var memberRole: GroupMemberRole = memberRole
        private set

    var profileImageUrl: String? = profileImageUrl
        private set

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
            updated()
        }
    }

    fun changeNickname(nickname: String?) {
        nickname?.let{
            this.nickname = nickname
            updated()
        }
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false
        other as GroupMember
        return (other.memberId == memberId) && (other.groupId == groupId)
    }

    override fun hashCode(): Int {
        return Objects.hash(memberId, groupId)
    }
}