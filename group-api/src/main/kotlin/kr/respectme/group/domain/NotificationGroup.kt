package kr.respectme.group.domain

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.domain.annotations.DomainRelation
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.ForbiddenException
import kr.respectme.common.error.NotFoundException
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.common.errors.GroupServiceErrorCode
import kr.respectme.group.domain.notifications.*
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
    @DomainRelation
    private val members: MutableSet<GroupMember> =  mutableSetOf(),
    @DomainRelation
    private val notifications: MutableSet<Notification> = mutableSetOf<Notification>(),
    private val createdAt: Instant = Instant.now(),
    private var name: String,
    private var description: String,
    private var ownerId: UUID,
    private var password: String? = null,
    private var type: GroupType = GroupType.GROUP_PRIVATE,
): BaseDomainEntity<UUID>(id) {

//    @IgnoreField
//    private val logger = LoggerFactory.getLogger(javaClass)

    fun getNotifications(): Set<Notification> {
        return notifications
    }

    fun getMembers(): Set<GroupMember> {
        return members
    }

    fun getCreatedAt(): Instant {
        return createdAt
    }

    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    fun getOwnerId(): UUID {
        return ownerId
    }

    fun getPassword(): String? {
        return password
    }

    fun getType(): GroupType {
        return type
    }

    fun changeGroupPassword(passwordEncoder: PasswordEncoder, password: String) {
        this.password = passwordEncoder.encode(password)
    }


    fun changeGroupType(groupType: GroupType?) {
        groupType?.let {
            this.type = groupType
        }
    }

    fun changeGroupName(name: String?) {
        name?.let {
            this.name = it
        }
    }

    fun changeGroupDescription(description: String?) {
        description?.let {
            this.description = it
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
        val owner =members.find { member -> member.getMemberId() == requestMemberId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)
        val member = members.find { member -> member.getMemberId() == newOwnerId }
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
        val member = members.find { member->member.getMemberId() == requestMemberId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)
        val notification = notifications.find{ it.id == notificationId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_NOTIFICATION_NOT_EXISTS)

        if(notification.getGroupId() == id && (member.getMemberId() == notification.getSenderId() || member.isGroupOwner())) {
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
        if(notification.getGroupId() != id) {
            throw ForbiddenException(GroupServiceErrorCode.GROUP_NOTIFICATION_GROUP_ID_MISMATCH)
        }

        val member = members.find { member -> member.getMemberId() == requestMemberId }
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
        val requestMember = members.find { member -> member.getMemberId() == requestMemberId }
            ?: throw NotFoundException(GroupServiceErrorCode.GROUP_MEMBER_NOT_FOUND)
        val targetMember = members.find { member -> member.getMemberId() == targetMemberId }
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