package kr.respectme.member.port.out.event

import kr.respectme.common.saga.SagaEvent
import kr.respectme.member.common.saga.event.MemberDeleteSaga
import java.util.*


interface MemberDeleteSagaEventPublishPort<T: SagaEvent<*>> {

    fun publish(eventName: String, payload: T)
}