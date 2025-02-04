package kr.respectme.auth.port.`in`.saga

import kr.respectme.auth.port.`in`.saga.event.MemberDeleteSaga
import kr.respectme.common.saga.SagaEvent
import org.springframework.kafka.support.Acknowledgment

interface MemberDeleteSagaListenPort {

    fun onMemberDelete(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onMemberDeleteFailed(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onMemberDeleteCompleted(message: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)
}