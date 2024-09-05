package kr.respectme.member.infrastructures.persistence.port

import kr.respectme.member.domain.model.Member


interface MemberSavePort {

    fun save(UUID: Member): Member

    fun delete(UUID: Member): Unit
}