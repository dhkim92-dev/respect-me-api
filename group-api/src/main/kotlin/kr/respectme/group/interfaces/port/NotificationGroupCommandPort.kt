package kr.respectme.group.interfaces.port

import kr.respectme.group.interfaces.dto.*
import java.util.UUID

interface NotificationGroupCommandPort {

    fun createNotificationGroup(loginId: UUID, request: GroupCreateRequest): NotificationGroupResponse

    fun updateNotificationGroup(loginId: UUID, groupId: UUID, request: GroupModifyRequest): NotificationGroupResponse

    fun addGroupMember(loginId: UUID, groupId: UUID, request: GroupMemberCreateRequest): GroupMemberResponse

    fun removeGroupMember(loginId: UUID, groupId: UUID, targetMemberId: UUID)

    fun deleteNotificationGroup(loginId: UUID,groupID: UUID)

    fun createNotification(loginId: UUID, groupId: UUID, request: NotificationCreateRequest): NotificationResponse
}