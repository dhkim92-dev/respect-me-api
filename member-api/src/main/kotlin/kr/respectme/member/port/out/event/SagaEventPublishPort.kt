package kr.respectme.member.port.out.event

import kr.respectme.common.saga.SagaEvent


interface SagaEventPublishPort {

    fun <T: SagaEvent<*>> publish(eventName: String, payload: T)
}