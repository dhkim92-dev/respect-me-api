package kr.respectme.group.port.out.persistence

import kr.respectme.group.adapter.out.persistence.entity.GroupNotificationQueryModel
import kr.respectme.group.adapter.out.persistence.entity.NotificationGroupQueryModel
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.notification.NotificationDto
import java.util.UUID

/**
 * Query Notification Group information include members, notifications
 */
interface QueryGroupPort {

    /**
     * Get NotificationGroup by ID
     * @param groupId NotificationGroup ID
     * @return NotificationGroupDto
     */
    fun getGroup(loginId: UUID, groupId: UUID): NotificationGroupQueryModel?

    /**
     * Get list of groups that login user is member of group
     * @param loginId login user ID
     * @return List<NotificationGroupDto>
     */
    fun getMemberGroups(loginId: UUID): List<NotificationGroupQueryModel>

//    /**
//     * Get GroupMember by GroupId and MemberId
//     * @param groupId NotificationGroup ID
//     * @param memberId Member ID
//     * @return GroupMemberDto or null
//     */
//    fun getGroupMember(groupId: UUID, memberId: UUID): GroupMemberDto?

//    /**
//     * Get GroupMembers by GroupId
//     * @param groupId NotificationGroup ID
//     * @return List<GroupMemberDto>
//     */
//    fun getGroupMembers(groupId: UUID): List<GroupMemberDto>

//    /**
//     * Get published notifications
//     * @param groupId NotificationGroup ID
//     * @param cursor notificationId of last notification
//     * @param size pagination size
//     * @return List<NotificationDto>
//     */
//    fun getPublishedNotifications(groupId: UUID, cursor: UUID?, size: Int): List<GroupNotificationQueryModel>

//    /**
//     * Get notification by GroupId and NotificationId
//     * @param groupId NotificationGroup ID
//     * @param notificationId Notification ID
//     * @return NotificationDto
//     */
//    fun getNotification(groupId: UUID, notificationId: UUID): GroupNotificationQueryModel?

    /**
     * Get all groups that group type is public
     * @param cursor group id for pagination
     * @param size size for pagination
     * @return List<NotificationGroupDto>
     */
    fun getAllGroups(cursor: UUID?, size: Int?): List<NotificationGroupQueryModel>

//    /**
//     * return all notifications that login user is member of group
//     * @param loginId login user ID
//     * @param cursor cursor for pagination
//     * @param size size for pagination
//     * @return List<NotificationDto>
//     */
//    fun getMemberNotifications(loginId: UUID, cursor: UUID?, size: Int): List<GroupNotificationQueryModel>
}