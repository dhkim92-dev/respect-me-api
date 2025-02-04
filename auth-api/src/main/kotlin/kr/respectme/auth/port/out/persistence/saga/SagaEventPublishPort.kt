package kr.respectme.auth.port.out.persistence.saga

import kr.respectme.auth.domain.MemberId
import kr.respectme.common.saga.SagaEvent
import java.util.UUID

interface SagaEventPublishPort {

    fun publish(eventName: String, payload: SagaEvent<*>)
}