package kr.respectme.auth.domain

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.slf4j.LoggerFactory

@Converter(autoApply = true)
class OidcPlatformConverter : AttributeConverter<OidcPlatform, Int> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun convertToDatabaseColumn(attribute: OidcPlatform): Int {
        logger.debug("convertToDatabaseColumn: $attribute")
        return attribute.value
    }

    override fun convertToEntityAttribute(dbData: Int): OidcPlatform {
        logger.debug("convert to entity attribute: $dbData")
        return OidcPlatform.values().first { it.value == dbData }
    }
}