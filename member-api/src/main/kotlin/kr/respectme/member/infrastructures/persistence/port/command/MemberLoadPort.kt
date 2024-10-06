package kr.respectme.member.infrastructures.persistence.port.command

import kr.respectme.member.domain.model.Member
import java.util.*

interface MemberLoadPort {

    fun getMemberById(id: java.util.UUID): Member?

    fun getMemberByEmail(email: String): Member?

    fun getMembersInList(memberIds: List<UUID>): List<Member>

    fun getMemberWithDeviceToken(memberId: UUID): Member?
}