package kr.respectme.auth.unit.domain

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.auth.domain.OidcPlatform
import kr.respectme.auth.domain.OidcPlatformConverter

class OidcPlatformConverterTest: AnnotationSpec() {

    private val converter = OidcPlatformConverter()

    @Test
    fun `ConvertToEntity test`() {
        // Given
        val platform = OidcPlatform.GOOGLE

        // When
        val result = converter.convertToEntityAttribute(platform.value)

        // Then
        result shouldBe OidcPlatform.GOOGLE
    }

    @Test
    fun `ConvertToDatabase test`() {
        // Given
        val platform = OidcPlatform.GOOGLE

        // When
        val result = converter.convertToDatabaseColumn(platform)

        // Then
        result shouldBe OidcPlatform.GOOGLE.value
    }
}