package kr.respectme.member.infrastructures.persistence.port

import kr.respectme.member.domain.model.Member

interface MemberLoadPort {

    fun getMemberById(id: java.util.UUID): Member?

    fun getMemberByEmail(email: String): Member?

    fun getMembersInList(UUIDIds: List<java.util.UUID>): List<Member>
}