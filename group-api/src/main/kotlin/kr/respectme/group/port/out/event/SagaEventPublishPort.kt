package kr.respectme.group.port.out.saga

import kr.respectme.common.saga.SagaEvent
import kr.respectme.group.application.event.MemberDeleteSaga

interface SagaEventPublishPort {

    fun <T: SagaEvent<*>> publish(eventName: String, message: T)
}