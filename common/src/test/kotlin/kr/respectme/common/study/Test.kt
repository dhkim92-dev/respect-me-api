package kr.respectme.common.study

import io.kotest.core.spec.style.AnnotationSpec
import org.springframework.web.util.UriComponentsBuilder

class Test: AnnotationSpec() {

    @Test
    fun `URLComponentTest`() {
        val base = "http://localhost"
        val builder = UriComponentsBuilder.fromHttpUrl(base)
        builder.queryParam("queryKey", "queryValue")

        println(builder.build().toUriString())

    }
}