package kr.respectme.group.infrastructures.event

import kr.respectme.group.infrastructures.event.dto.NotificationSentEvent

interface EventSubscribePort {

    fun onReceiveNotificationSent(event: NotificationSentEvent)
}