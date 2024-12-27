package kr.respectme.member.port.`in`

import java.util.*

interface MemberCommandPort {

    fun deleteMember(loginId: UUID, resourceId: UUID): Unit
}