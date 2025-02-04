package kr.respectme.member.port.out.event.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kr.respectme.member.port.out.event.TransactionType

@Converter
class TransactionTypeConverter: AttributeConverter<TransactionType, Int> {

    override fun convertToDatabaseColumn(attribute: TransactionType?): Int {
        return attribute?.value ?: throw IllegalArgumentException("TransactionType is null")
    }

    override fun convertToEntityAttribute(dbData: Int?): TransactionType {
        return TransactionType.values().firstOrNull { it.value == dbData } ?: throw IllegalArgumentException("TransactionType is null")
    }
}