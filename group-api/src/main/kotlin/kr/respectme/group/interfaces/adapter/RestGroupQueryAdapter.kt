package kr.respectme.group.interfaces.adapter

import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.query.useCase.NotificationGroupQueryUseCase
import kr.respectme.group.interfaces.dto.GroupMemberVo
import kr.respectme.group.interfaces.dto.GroupNotificationVo
import kr.respectme.group.interfaces.dto.NotificationGroupVo
import kr.respectme.group.interfaces.port.NotificationGroupQueryPort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
@RequestMapping("/api/v1/")
@Tag(name = "NotificationGroup", description = "알림 그룹 API")
@SecurityRequirement(name = "bearer-jwt")
class RestGroupQueryAdapter(private val queryUseCase: NotificationGroupQueryUseCase)
: NotificationGroupQueryPort {

    @GetMapping("notification-groups/{groupId}/members")
    @ApplicationResponse(status = HttpStatus.OK, message = "group members retrieved.")
    @CursorPagination
    override fun getGroupMembers(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestParam(required = false) @CursorParam(key="id") cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<GroupMemberVo> {
        return queryUseCase.retrieveGroupMembers(loginId, groupId)
            .map { it -> GroupMemberVo.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}/notifications")
    @ApplicationResponse(status = HttpStatus.OK, message = "group notifications retrieved.")
    @CursorPagination
    override fun getGroupNotifications(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestParam(required = false) @CursorParam(key="notificationId") cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<GroupNotificationVo> {
        return queryUseCase.retrieveGroupNotifications(loginId, groupId, cursor, size ?: 10)
            .map { it -> GroupNotificationVo.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}/members/{memberId}")
    @ApplicationResponse(status = HttpStatus.OK, message = "group member retrieved.")
    override fun getMember(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @PathVariable memberId: UUID
    ): GroupMemberVo {
        return GroupMemberVo.valueOf(
            queryUseCase.retrieveGroupMember(loginId, groupId, memberId)
        )
    }

    @GetMapping("group-members/me/notification-groups")
    @CursorPagination
    @ApplicationResponse(status = HttpStatus.OK, message = "my groups retrieved.")
    override fun getMyGroups(
        @LoginMember loginId: UUID,
        @RequestParam(required = false) @CursorParam(key="id") cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<NotificationGroupVo> {
        return queryUseCase.retrieveMemberGroups(loginId)
            .map { it -> NotificationGroupVo.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}")
    @ApplicationResponse(status = HttpStatus.OK, message = "group retrieved.")
    override fun getNotificationGroup(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID
    ): NotificationGroupVo {
        return NotificationGroupVo.valueOf(
            queryUseCase.retrieveGroup(loginId, groupId)
        )
    }

    @GetMapping("notification-groups")
    @ApplicationResponse(status = HttpStatus.OK, message = "all groups retrieved.")
    @CursorPagination
    override fun getAllGroups(
        @LoginMember loginId: UUID,
        @CursorParam(key="groupId") @RequestParam(required = false) groupId: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<NotificationGroupVo> {
        return queryUseCase.retrieveAllGroups(loginId, groupId, size)
            .map { it -> NotificationGroupVo.valueOf(it) }
    }
}