package kr.respectme.group.infrastructures.event

import java.beans.EventHandler

interface EventSubscribePort {

    fun handleNotificationCompleteEvent(data: String)
}