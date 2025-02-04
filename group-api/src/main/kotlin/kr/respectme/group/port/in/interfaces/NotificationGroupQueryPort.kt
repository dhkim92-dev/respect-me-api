package kr.respectme.group.port.`in`.interfaces

import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import kr.respectme.group.port.`in`.interfaces.dto.GroupNotificationQueryResponse
import kr.respectme.group.port.`in`.interfaces.dto.NotificationGroupQueryResponse
import java.util.UUID

interface NotificationGroupQueryPort {

    /**
     * 그룹의 요약 정보를 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param groupId: 조회할 그룹 ID
     * @return NotificationGroupSummaryDto
     */
    fun getNotificationGroup(loginId: UUID, groupId: UUID): NotificationGroupQueryResponse

    /**
     * 그룹의 알림을 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param groupId: 조회할 그룹 ID
     * @param cursor: 다음 페이지를 조회하기 위한 커서 Notification ID에 해당한다.
     * @param size: 한 페이지에 조회할 알림 수
     */
    fun getGroupNotifications(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int?): List<GroupNotificationQueryResponse>

    /**
     * 그룹의 회원을 목록을 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param groupId: 조회할 그룹 ID
     * @param cursor: 다음 페이지를 조회하기 위한 커서 Member ID에 해당한다.
     * @param size: 한 페이지에 조회할 그룹 수
     */
    fun getGroupMembers(loginId: UUID, groupId: UUID, cursor: UUID?, size: Int?): List<GroupMemberVo>

    /**
     * 그룹의 회원을 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param groupId: 조회할 그룹 ID
     * @param memberId: 조회할 회원 ID
     * @return GroupMemberVo
     */
    fun getMember(loginId: UUID, groupId: UUID, memberId: UUID): GroupMemberVo

    /**
     * 내가 속한 그룹을 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param cursor: 다음 페이지를 조회하기 위한 커서 Group ID에 해당한다.
     * @param size: 한 페이지에 조회할 그룹 수
     * @return List<NotificationGroupVo>
     */
    fun getMyGroups(loginId: UUID, cursor: UUID?, size: Int?): List<NotificationGroupQueryResponse>

    /**
     * 모든 그룹을 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param cursor: 다음 페이지를 조회하기 위한 커서 Group ID에 해당한다.
     * @param size: 한 페이지에 조회할 그룹 수
     * @return List<NotificationGroupVo>
    **/
    fun getAllGroups(loginId: UUID, cursor: UUID?, size: Int?): List<NotificationGroupQueryResponse>

    /**
     * 특정 멤버가 속한 그룹들의 모든 알림을 조회합니다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param cursor: 다음 페이지를 조회하기 위한 커서, NotificationId 에 해당한다.
     * @param size: 한 페이지에 조회할 그룹 수
     * @return List<NotificationGroupVo>
     */
    fun getMemberNotifications(loginId: UUID, cursor: UUID?, size: Int?): List<GroupNotificationQueryResponse>

    /**
     * 단일 알림을 상세 조회한다.
     * @param loginId: Access Token을 통해 확보한 사용자 ID
     * @param groupId: 조회할 그룹 ID
     * @param notificationId: 조회할 알림 ID
     */
    fun getNotification(loginId: UUID, groupId: UUID, notificationId: UUID): GroupNotificationQueryResponse
}