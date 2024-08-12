package kr.respectme.member_api.infrastructures.port

import kr.respectme.member_api.domain.model.Member


interface MemberSavePort {

    fun save(member: Member): Member

    fun delete(member: Member): Unit
}