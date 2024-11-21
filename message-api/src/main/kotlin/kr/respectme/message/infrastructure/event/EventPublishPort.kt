package kr.respectme.message.infrastructure.event

interface EventPublishPort {

    fun publish(eventName: String, event: Any): Boolean
}