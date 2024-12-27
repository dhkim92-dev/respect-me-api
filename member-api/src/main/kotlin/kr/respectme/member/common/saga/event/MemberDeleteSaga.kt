package kr.respectme.member.common.saga.event

import java.util.UUID

data class MemberDeleteSaga(
    val eventVersion: Int = 1,
    val memberId: UUID
) {

    override fun toString(): String {
        return "MemberDeleteSaga(eventVersion=$eventVersion, memberId=$memberId)"
    }
}