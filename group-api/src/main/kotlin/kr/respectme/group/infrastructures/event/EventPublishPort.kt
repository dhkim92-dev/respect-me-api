package kr.respectme.group.infrastructures.event

interface EventPublishPort {

    fun publish(eventName: String, message: Any)
}