package kr.respectme.member.adapter.out.persistence.jpa

import jakarta.persistence.AttributeConverter
import kr.respectme.member.domain.model.MemberRole

class MemberRoleConverter: AttributeConverter<MemberRole, Int> {

    override fun convertToDatabaseColumn(attribute: MemberRole?): Int {
        return attribute?.dbValue ?: throw IllegalArgumentException("MemberRole is null")
    }

    override fun convertToEntityAttribute(dbData: Int?): MemberRole {
        return MemberRole.values().firstOrNull { it.dbValue == dbData } ?: throw IllegalArgumentException("MemberRole is null")
    }
}