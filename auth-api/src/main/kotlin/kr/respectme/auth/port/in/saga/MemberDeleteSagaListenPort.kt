package kr.respectme.auth.port.`in`.saga

import kr.respectme.auth.port.`in`.saga.event.MemberDeleteSaga
import kr.respectme.common.saga.Saga
import org.springframework.kafka.support.Acknowledgment

interface MemberDeleteSagaListenPort {

    fun onMemberDelete(message: Saga<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onMemberDeleteFailed(message: Saga<MemberDeleteSaga>, acknowledgment: Acknowledgment)

    fun onMemberDeleteCompleted(message: Saga<MemberDeleteSaga>, acknowledgment: Acknowledgment)
}