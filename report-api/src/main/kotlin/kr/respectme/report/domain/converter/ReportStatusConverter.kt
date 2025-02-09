package kr.respectme.report.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kr.respectme.report.domain.ReportStatus

@Converter(autoApply = true)
class ReportStatusConverter(): AttributeConverter<ReportStatus, Int>{

    override fun convertToDatabaseColumn(p0: ReportStatus?): Int {
        if(p0 == null) {
            throw IllegalStateException("ReportStatus is null")
        }

        return p0.value
    }

    override fun convertToEntityAttribute(p0: Int?): ReportStatus {
        if(p0 == null) {
            throw IllegalStateException("ReportStatus is null")
        }

        return ReportStatus.entries.firstOrNull { it.value == p0 }
            ?: throw IllegalStateException("ReportStatus is null")
    }
}