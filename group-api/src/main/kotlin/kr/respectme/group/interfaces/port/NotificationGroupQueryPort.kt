package kr.respectme.group.interfaces.port

import kr.respectme.group.application.dto.group.NotificationGroupSummaryDto
import kr.respectme.group.application.dto.member.GroupMemberDto
import kr.respectme.group.interfaces.dto.GroupMemberVo
import kr.respectme.group.interfaces.dto.GroupNotificationVo
import kr.respectme.group.interfaces.dto.NotificationGroupVo
import java.util.UUID

interface NotificationGroupQueryPort {

    fun getNotificationGroup(loginId: UUID, groupId: UUID): NotificationGroupVo

    fun getGroupNotifications(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int?): List<GroupNotificationVo>

    fun getGroupMembers(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int?): List<GroupMemberVo>

    fun getMember(loginId: UUID, groupId: UUID, memberId: UUID): GroupMemberVo

    fun getMyGroups(loginId: UUID, cursor: UUID?, size: Int?): List<NotificationGroupVo>

    fun getAllGroups(loginId: UUID, groupId: UUID?, size: Int?): List<NotificationGroupVo>
}