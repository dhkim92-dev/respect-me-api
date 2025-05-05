package kr.respectme.common.response.hateoas

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.common.support.response.SampleHateoasConverter
import kr.respectme.common.support.response.SampleHateoasElement
import java.util.UUID


class HateoasableConverterTest : AnnotationSpec() {
    private val converter = SampleHateoasConverter()

    @Test
    fun `SampleHateoasElement Link 생성 테스트`() {
        val element = SampleHateoasElement(id = UUID.randomUUID())

        val converted = converter.convert(element)._links[0]

        converted.rel shouldBe "self"
        converted.href shouldBe "https://www.noti-me.net/samples/${element.id}"
    }
}