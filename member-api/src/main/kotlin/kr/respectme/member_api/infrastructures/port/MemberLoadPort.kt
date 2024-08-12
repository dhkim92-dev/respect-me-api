package kr.respectme.member_api.infrastructures.port

import kr.respectme.member_api.domain.model.Member
import java.util.*

interface MemberLoadPort {

    fun getMemberById(id: UUID): Member?

    fun getMemberByEmail(email: String): Member?

    fun getMembersInList(memberIds: List<UUID>): List<Member>
}