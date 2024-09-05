package kr.respectme.member.infrastructures.persistence.jpa

import jakarta.persistence.AttributeConverter
import kr.respectme.member.domain.model.DeviceTokenType

class DeviceTokenTypeConverter: AttributeConverter<DeviceTokenType, Int> {

    override fun convertToDatabaseColumn(attribute: DeviceTokenType?): Int? {
        return attribute?.dbValue
    }

    override fun convertToEntityAttribute(dbData: Int?): DeviceTokenType? {
        return DeviceTokenType.values().find { it.dbValue == dbData }
    }
}