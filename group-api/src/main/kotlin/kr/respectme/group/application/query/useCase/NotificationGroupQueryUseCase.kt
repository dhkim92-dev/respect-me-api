package kr.respectme.group.application.query.useCase

import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.application.dto.group.NotificationGroupDto
import kr.respectme.group.application.dto.notification.NotificationDto
import java.util.UUID

/***
 * NotificationGroup UseCase for only query
 */
interface NotificationGroupQueryUseCase {

    /**
     * retrieve group member
     * @param loginId login user id
     * @param groupId group id
     * @param memberId target member id want to retrieve info
     * @return GroupMemberDto
     */
    fun retrieveGroupMember(loginId: UUID, groupId: UUID, memberId: UUID): GroupMemberDto

    /**
     * retrieve group members
     * if group doesn't contain login user, throw ForbiddenException
     * @param loginId login user id
     * @param groupId group id
     * @return List<GroupMemberDto>
     */
    fun retrieveGroupMembers(loginId: UUID, groupId: UUID): List<GroupMemberDto>

    /**
     * retrieve group notifications
     * if group doesn't contain login user, throw ForbiddenException
     * @param loginId login user id
     * @param groupId group id
     * @param cursor cursor for pagination
     * @param size size for pagination
     * @return List<NotificationDto>
     */
    fun retrieveGroupNotifications(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int): List<NotificationDto>

    /**
     * retrieve group by given group id
     * if group is private, check login user is member of group
     * if not, throw ForbiddenException
     * @param loginId login user id
     * @param groupId group id
     * @return NotificationGroupDto
     */
    fun retrieveGroup(loginId: UUID, groupId: UUID): NotificationGroupDto

    /**
     * retrieve groups that login user is member of group
     * @param loginId login user id
     * @return List<NotificationGroupDto>
     */
    fun retrieveMemberGroups(loginId: UUID): List<NotificationGroupDto>

    /**
     * retrieve all groups that group type is public.
     * @param loginId login user id
     * @param cursorGroupId cursor group id for pagination
     * @param size size for pagination
     * @return List<NotificationGroupDto>
     */
    fun retrieveAllGroups(loginId: UUID,
                          cursorGroupId: UUID?,
                          size: Int?): List<NotificationGroupDto>
}