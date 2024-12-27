package kr.respectme.member.port.out.persistence.command

import kr.respectme.member.domain.model.Member


interface MemberSavePort {

    fun save(UUID: Member): Member

    fun delete(UUID: Member): Unit
}