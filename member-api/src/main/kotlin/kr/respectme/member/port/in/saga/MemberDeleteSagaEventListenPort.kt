package kr.respectme.member.port.`in`.saga

import kr.respectme.common.saga.Saga
import kr.respectme.member.common.saga.event.MemberDeleteSaga
import org.springframework.kafka.support.Acknowledgment

interface MemberDeleteSagaEventListenPort {

    fun onMemberDeleteSagaCompleted(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    )

    fun onMemberDeleteSagaFailed(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    )

    fun onMemberDeleteSagaAuthServiceCompleted(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    )

    fun onMemberDeleteSagaAuthServiceFailed(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    )

    fun onMemberDeleteSagaGroupServiceComplete(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    )

    fun onMemberDeleteSagaGroupServiceFailed(
        message: Saga<MemberDeleteSaga>,
        acknowledgment: Acknowledgment
    )
}