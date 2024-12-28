package kr.respectme.auth.port.`in`.saga.event

import java.util.UUID

data class MemberDeleteSaga(
    val eventVersion: Int = 1,
    val memberId: UUID,
) {

}