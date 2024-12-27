package kr.respectme.member.port.out.saga

import kr.respectme.member.common.saga.event.MemberDeleteSaga
import java.util.*


interface MemberDeleteSagaEventPublishPort {

    fun memberDeleteSagaStart(saga: MemberDeleteSaga)

    fun memberDeleteSagaCompleted(transactionId: UUID, saga: MemberDeleteSaga)

    fun memberDeleteSagaFailed(transactionId: UUID, saga: MemberDeleteSaga)
}