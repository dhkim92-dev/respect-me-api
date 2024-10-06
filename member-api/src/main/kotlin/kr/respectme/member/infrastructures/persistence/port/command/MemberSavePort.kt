package kr.respectme.member.infrastructures.persistence.port.command

import kr.respectme.member.domain.model.Member


interface MemberSavePort {

    fun save(UUID: Member): Member

    fun delete(UUID: Member): Unit
}