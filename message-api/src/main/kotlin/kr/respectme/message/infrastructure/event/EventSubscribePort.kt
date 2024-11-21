package kr.respectme.message.infrastructure.event

import kr.respectme.message.infrastructure.event.dto.NotificationCreateEvent

interface EventSubscribePort {

    fun onNotificationCreateEvent(event: NotificationCreateEvent): Boolean
}