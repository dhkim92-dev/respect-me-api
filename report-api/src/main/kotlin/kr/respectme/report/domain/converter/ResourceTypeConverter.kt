package kr.respectme.report.domain.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import kr.respectme.report.domain.ResourceType

@Converter(autoApply = true)
class ResourceTypeConverter : AttributeConverter<ResourceType, Int> {

    override fun convertToDatabaseColumn(p0: ResourceType?): Int {
        if(p0 == null) {
            throw IllegalStateException("ResourceType is null")
        }

        return p0.value
    }

    override fun convertToEntityAttribute(p0: Int?): ResourceType {
        if(p0 == null) {
            throw IllegalStateException("ResourceType is null")
        }

        return ResourceType.entries.firstOrNull { it.value == p0 }
            ?: throw IllegalStateException("ResourceType is null")
    }
}