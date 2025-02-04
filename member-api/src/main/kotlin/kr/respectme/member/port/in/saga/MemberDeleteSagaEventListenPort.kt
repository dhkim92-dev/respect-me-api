package kr.respectme.member.port.`in`.saga

import kr.respectme.common.saga.SagaEvent
import kr.respectme.member.common.saga.event.MemberDeleteSaga
import org.springframework.kafka.support.Acknowledgment

interface MemberDeleteSagaEventListenPort {

    fun onMemberDeleteCompleted(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onMemberDeleteFailed(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onGroupServiceCompleted(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onGroupServiceFailed(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onAuthServiceCompleted(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onAuthServiceFailed(event: SagaEvent<MemberDeleteSaga>, acknowledgment: Acknowledgment)
}