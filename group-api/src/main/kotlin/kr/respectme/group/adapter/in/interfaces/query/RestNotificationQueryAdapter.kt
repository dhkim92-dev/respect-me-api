package kr.respectme.group.adapter.`in`.interfaces.query

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import kr.respectme.common.annotation.LoginMember
import kr.respectme.group.application.dto.notification.NotificationCountDto
import kr.respectme.group.application.query.useCase.NotificationQueryUseCase
import kr.respectme.group.port.`in`.interfaces.NotificationQueryPort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/")
@Tags(Tag(name = "Notification API", description = "그룹 알림 조회 API"))
class RestNotificationQueryAdapter(
    private val notificationQueryUsecase: NotificationQueryUseCase
): NotificationQueryPort {

    @Operation(summary = "오늘 그룹에서 추가 발행 가능한 알림 수 반환", description = "오늘 발송된 그룹의 잔여 알림 수를 반환한다. 시간은 UTC 기준이며, YYYY-MM-DD 00:00:00 ~ 23:59:59 까지의 발행된 알림 수를 반환한다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "그룹의 잔여 알림 개수 조회 성공"),
    ])
    @GetMapping("/notification-groups/{groupId}/notification-left-count")
    override fun getTodayNotificationCount(@LoginMember loginId: UUID, @PathVariable groupId: UUID)
    : NotificationCountDto {
        return notificationQueryUsecase.getTodayNotificationCount(groupId)
    }
}