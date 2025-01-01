package kr.respectme.auth.port.out.persistence.saga

import kr.respectme.auth.domain.MemberId
import java.util.UUID

interface AuthInfoDeleteSagaPublishPort {

    fun publishAuthInfoDeleteCompletedSaga(transactionId: UUID, memberId: MemberId)

    fun publishAuthInfoDeleteFailedSaga(transactionId: UUID, memberId: MemberId)
}