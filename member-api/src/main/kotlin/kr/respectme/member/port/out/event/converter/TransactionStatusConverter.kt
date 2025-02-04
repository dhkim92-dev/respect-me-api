package kr.respectme.member.port.out.event.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kr.respectme.member.port.out.event.TransactionStatus

@Converter(autoApply = true)
class TransactionStatusConverter: AttributeConverter<TransactionStatus, Int> {

    override fun convertToDatabaseColumn(attribute: TransactionStatus?): Int {
        return attribute?.value ?: throw IllegalArgumentException("TransactionStatus is null")
    }

    override fun convertToEntityAttribute(dbData: Int?): TransactionStatus {
        return TransactionStatus.values().firstOrNull { it.value == dbData } ?: throw IllegalArgumentException("TransactionStatus is null")
    }
}