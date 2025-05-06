package kr.respectme.common.response

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.respectme.common.advice.HateoasAdvice
import kr.respectme.common.advice.hateoas.HateoasResponse
import kr.respectme.common.support.response.*
import org.springframework.context.ApplicationContext
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.http.server.ServletServerHttpResponse
import java.util.*

class HateoasAdviceTest : AnnotationSpec() {

    private val applicationContext = mockk<ApplicationContext>()
    private val hateoasAdvice = HateoasAdvice(applicationContext)
    private val returnType =  mockk<MethodParameter>()
    private val mediaType = mockk<MediaType>()
    private val converterType = HttpMessageConverter::class.java
    private val httpRequest = mockk<ServletServerHttpRequest>()
    private val httpResponse = mockk<ServletServerHttpResponse>()

    private fun createSamples(size: Int): List<SampleHateoasElement> {
        return List(size) { idx -> SampleHateoasElement(UUID.randomUUID()) }

    }

    private fun callAdvice(data: Any): Any? {
        return hateoasAdvice.beforeBodyWrite(
            data,
            returnType,
            mediaType,
            converterType,
            httpRequest,
            httpResponse
        )
    }

    @BeforeEach
    fun setUp() {
        every { applicationContext.getBean(SampleHateoasConverter::class.java) } returns SampleHateoasConverter()
        every { applicationContext.getBean(InnerElementHateoasConverter::class.java) } returns InnerElementHateoasConverter()
        every { applicationContext.getBean(OutterElementHateoasConverter::class.java) } returns OutterElementHateoasConverter()
    }

    @Test
    fun `단일 객체 변환 테스트`() {
        val element = SampleHateoasElement(id = UUID.randomUUID())

        val converted =  callAdvice(element)
        (converted is HateoasResponse) shouldBe true
        converted as HateoasResponse
        val link = converted._links[0]
        link.rel shouldBe "self"
        link.href shouldBe "https://www.noti-me.net/samples/${element.id}"
    }

    @Test
    fun `List 객체 변환 테스트`() {
        val listElement = List(10) { SampleHateoasElement(
            id = UUID.randomUUID(),
        ) }

        val converted = callAdvice(listElement)
        (converted is List<*>) shouldBe  true
        converted as List<out HateoasResponse>
        converted.forEachIndexed { idx, elem ->
            val link = elem._links[0]
            link.rel shouldBe "self"
            link.href shouldBe "https://www.noti-me.net/samples/${listElement[idx].id}"
        }
    }

    @Test
    fun `Nested Translate 테스트`() {
        val innerElements = List(10) {
            InnerElement(id = UUID.randomUUID())
        }

        val outerElement = OutterElement(id = UUID.randomUUID(), inner = innerElements)
        val converted = callAdvice(outerElement)
        (converted is OutterElement) shouldBe true
        converted as OutterElement

        val outLink = converted._links[0]
        outLink.rel shouldBe "self"
        outLink.href shouldBe "https://www.noti-me.net/api/v1/outters/${outerElement.id}"

        val innerLink = innerElements[0]._links[0]

        innerLink.rel shouldBe "self"
        innerLink.href shouldBe "https://www.noti-me.net/api/v1/inners/${innerElements[0].id}"
    }
}