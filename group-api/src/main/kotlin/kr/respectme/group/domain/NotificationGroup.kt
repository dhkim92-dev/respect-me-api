package kr.respectme.group.domain

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.domain.annotations.DomainRelation
import kr.respectme.common.domain.annotations.IgnoreField
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.notifications.*
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.util.*

/**
 * Notification Group Entity
 * Domain constraints
 * 1. Notification Group has unique id
 * 2. Notification Group has members, if members empty, group should be deleted
 * 3. Notification Group can be removed, but if the member is admin, only owner can remove.
 * 4. Notification Group should have owner. if owner want to leave the group, owner should change the owner to another member.
 * @property id UUID
 * @property members group members
 * @property notifications group notifications
 * @property createdAt created time
 * @property name group name
 * @property description group description
 * @property ownerId group owner id
 * @property password group password (if group is private required)
 */
@DomainEntity
class NotificationGroup(
    id: UUID = UUIDV7Generator.generate(),
    members: MutableSet<GroupMember> =  mutableSetOf(),
    notifications: MutableSet<Notification> = mutableSetOf<Notification>(),
    createdAt: Instant = Instant.now(),
    name: String,
    description: String,
    ownerId: UUID,
    password: String? = null,
    type: GroupType = GroupType.GROUP_PRIVATE,
): BaseDomainEntity<UUID>(id) {

    var name: String = name
        private set

    var description: String = description
        private set

    var ownerId: UUID = ownerId
        private set

    var type: GroupType = type
        private set

    var password: String? = password
        private set

    @DomainRelation
    val members: MutableSet<GroupMember> = members

    @DomainRelation
    val notifications: MutableSet<Notification> = notifications

    val createdAt: Instant = createdAt

//    @IgnoreField
//    private val logger = LoggerFactory.getLogger(javaClass)

    fun changeGroupType(groupType: GroupType?) {
        groupType?.let {
            this.type = groupType
//            updated()
        }
    }

    fun changeGroupName(name: String?) {
        name?.let {
            this.name = it
//            updated()
        }
    }

    fun changeGroupDescription(description: String?) {
        description?.let {
            this.description = it
//            updated()
        }
    }

    fun isPrivate(): Boolean {
        return type == GroupType.GROUP_PRIVATE
    }

    /**
     * Change group owner
     * @param requestMemberId member ID want to change owner
     * @param newOwnerId new owner ID
     */
    fun changeGroupOwner(requestMemberId: UUID, newOwnerId: UUID) {
        val owner =members.find { it.memberId == requestMemberId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)
        val member = members.find { member -> member.memberId == newOwnerId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        if(!owner.isGroupOwner()) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_OWNER)
        }

        member.changeMemberRole(GroupMemberRole.OWNER)
        owner.changeMemberRole(GroupMemberRole.MEMBER)
        ownerId = newOwnerId
//        updated()
    }

    fun changePassword(passwordEncoder: PasswordEncoder, password: String?) {
        password?.let{
            this.password = passwordEncoder.encode(password)
//            updated()
        }
    }

    fun removeNotification(requestMemberId: UUID, notificationId: UUID) {
        val member = members.find { it.memberId == requestMemberId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)
        val notification = notifications.find{ it.id == notificationId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if(notification.groupId == id && (member.memberId == notification.senderId || member.isGroupOwner())) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        notifications.remove(notification)
    }

    /**
     * Only group admin or owner can make notification
     * @param requestMemberId member ID want to make notification
     * @param notification notification to make
     */
    fun addNotification(requestMemberId: UUID, notification: Notification) {
        if(notification.groupId != id) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_GROUP_ID_MISMATCH)
        }

        val member = members.find { it.memberId == requestMemberId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        if(member.isGroupMember()) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        }

        if(notifications.contains(notification)) {
            notifications.remove(notification)
            notification.validate()
//            notification.updated()
        }

        notifications.add(notification)
    }

    fun addMember(member: GroupMember) {
        if(members.contains(member)) {
            members.remove(member)
//            member.updated()
        }
        members.add(member)
    }

    /**
     * remove member from this group
     * group owner can't leave the group until change the owner to another member
     * group admin can't remove owner or admin except itself
     * group member can't remove other member
     * @param requestMemberId member ID want to remove member
     * @param targetMemberId member ID want to remove
     */
    fun removeMember(requestMemberId: UUID, targetMemberId: UUID) {
        val requestMember = members.find { it.memberId == requestMemberId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)
        val targetMember = members.find { it.memberId == targetMemberId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)

        if ((requestMember != targetMember) &&
            requestMember.isGroupMember() ||
            (requestMember.isGroupAdmin() && (targetMember.isGroupOwner() || targetMember.isGroupAdmin()))
        ) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_MEMBER_NOT_ENOUGH_PERMISSION)
        } else if(targetMember.isGroupOwner() && members.size > 1) {
            throw BadRequestException(GroupServiceErrorCode.GROUP_OWNER_CANT_LEAVE)
        }
        members.remove(targetMember)
//        targetMember.removed()
    }
}