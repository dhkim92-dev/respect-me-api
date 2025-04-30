package kr.respectme.file.domain.converter

import jakarta.persistence.AttributeConverter
import kr.respectme.file.domain.enums.FileFormat

class FileFormatConverter: AttributeConverter<FileFormat, Int> {

    override fun convertToDatabaseColumn(attribute: FileFormat): Int {
        return attribute.value
    }

    override fun convertToEntityAttribute(dbData: Int?): FileFormat {
        return FileFormat.entries.find { it.value == dbData }
            ?: throw IllegalArgumentException("Unknown File Format value: $dbData")
    }
}