package kr.respectme.group.adapter.`in`.interfaces.query

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.query.useCase.NotificationGroupQueryUseCase
import kr.respectme.group.port.`in`.interfaces.vo.GroupMemberVo
import kr.respectme.group.port.`in`.interfaces.dto.GroupNotificationQueryResponse
import kr.respectme.group.port.`in`.interfaces.dto.NotificationGroupQueryResponse
import kr.respectme.group.port.`in`.interfaces.NotificationGroupQueryPort
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
    @Operation(summary = "지정된 Group 의 회원 목록을 반환.", description = "지정된 Group 의 회원 목록을 CursorList 타입으로 래핑하여 반환합니다." +
            "<br/> CursorList 는 다음 페이지가 존재하면 next 필드에 다음 페이지의 URL을 포함합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재하지 않으면 403 Forbidden을 반환합니다.")
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
    @Operation(summary = "지정된 Group 의 알림 목록을 반환.", description = "지정된 Group 의 알림 목록을 CursorList 타입으로 래핑하여 반환합니다." +
            "<br/> CursorList 는 다음 페이지가 존재하면 next 필드에 다음 페이지의 URL을 포함합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재하지 않으면 403 Forbidden을 반환합니다.")
    @ApplicationResponse(status = HttpStatus.OK, message = "group notifications retrieved.")
    @CursorPagination
    override fun getGroupNotifications(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @RequestParam(required = false) @CursorParam(key="notificationId") cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<GroupNotificationQueryResponse> {
        return queryUseCase.retrieveGroupNotifications(loginId, groupId, cursor, size ?: 10)
            .map { it -> GroupNotificationQueryResponse.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}/members/{memberId}")
    @ApplicationResponse(status = HttpStatus.OK, message = "group member retrieved.")
    @Operation(summary = "지정된 Group 의 회원을 반환.", description = "지정된 Group 의 회원 정보를 반환합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재해야 하며 그렇지 않을 경우 403 Forbidden이 반환됩니다.")
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
    @Operation(summary = "내가 속한 그룹 목록을 반환.", description = "내가 속한 그룹 목록을 CursorList 타입으로 래핑하여 반환합니다." +
            "<br/> CursorList 는 다음 페이지가 있는지 여부와 다음 페이지의 Cursor(next) 를 포함합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재하지 않으면 403 Forbidden을 반환합니다.")
    @CursorPagination
    @ApplicationResponse(status = HttpStatus.OK, message = "my groups retrieved.")
    override fun getMyGroups(
        @LoginMember loginId: UUID,
        @RequestParam(required = false) @CursorParam(key="id") cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<NotificationGroupQueryResponse> {
        return queryUseCase.retrieveMemberGroups(loginId)
            .map { it -> NotificationGroupQueryResponse.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}")
    @Operation(summary = "지정된 Group 의 정보를 반환.", description = "지정된 Group 의 정보를 반환합니다." +
            "<br/> 제공된 Access Token의 MemberId(sub)가 Group에 존재하지 않으면 403 Forbidden을 반환합니다.")
    @ApplicationResponse(status = HttpStatus.OK, message = "group retrieved.")
    override fun getNotificationGroup(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID
    ): NotificationGroupQueryResponse {
        return NotificationGroupQueryResponse.valueOf(
            queryUseCase.retrieveGroup(loginId, groupId)
        )
    }

    @GetMapping("notification-groups")
    @Operation(summary = "모든 그룹 목록을 반환.", description = "모든 그룹 목록을 CursorList 타입으로 래핑하여 반환합니다." +
            "<br/> CursorList 는 다음 페이지가 있을 경우 next 필드에 다음 페이지 조회 URL을 포함합니다.")
    @ApplicationResponse(status = HttpStatus.OK, message = "all groups retrieved.")
    @CursorPagination
    override fun getAllGroups(
        @LoginMember loginId: UUID,
        @CursorParam(key="id") @RequestParam(required = false) cursor: UUID?,
        @RequestParam(required = false, defaultValue = "20") size: Int?
    ): List<NotificationGroupQueryResponse> {
        return queryUseCase.retrieveAllGroups(loginId, cursor, size)
            .map { it -> NotificationGroupQueryResponse.valueOf(it) }
    }

    @GetMapping("notification-groups/{groupId}/notifications/{notificationId}")
    @Operation(summary = "그룹의 단일 노티피케이션을 상세 조회합니다.", description = "이 API를 사용해서 조회하는 경우 content 필드가 요약되지 않고 전체 내용을 포함합니다")
    @ApplicationResponse(status = HttpStatus.OK, message = "group notification retrieved.")
    override fun getNotification(
        @LoginMember loginId: UUID,
        @PathVariable groupId: UUID,
        @PathVariable notificationId: UUID
    ): GroupNotificationQueryResponse {
        return GroupNotificationQueryResponse.valueOf(
            queryUseCase.retrieveNotification(loginId, groupId, notificationId)
        )
    }

    @GetMapping("group-members/me/notifications")
    @Operation(summary = "내가 속한 그룹의 모든 알림을 조회합니다.", description = "내가 속한 그룹의 모든 알림을 조회합니다.")
    @ApplicationResponse(status = HttpStatus.OK, message = "member notifications retrieved.")
    @CursorPagination
    override fun getMemberNotifications(
        @LoginMember loginId: UUID,
        @CursorParam(key = "notificationId") cursor: UUID?,
        @RequestParam(defaultValue = "20") size : Int?
    ): List<GroupNotificationQueryResponse> {
        return queryUseCase.retrieveMemberNotifications(loginId, cursor, size?:20)
            .map { it -> GroupNotificationQueryResponse.valueOf(it) }
            .toList()
    }
}