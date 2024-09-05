package kr.respectme.member.study

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.AnnotationSpec
import kr.respectme.common.response.ApiResult
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ParameterizedTypeReference
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest
class WebClientStudy: AnnotationSpec() {
    private val webClient = WebClient.create()
    private val objectMapper = ObjectMapper()
    @Test
    fun sendRequest() {
        try {
            val response = webClient.get()
                .uri("https://api.github.com/users/asdfklajsdl11231231")
                .retrieve()
                .onStatus({ status -> status.is4xxClientError || status.is5xxServerError },
                    { response ->
                        response.bodyToMono(String::class.java)
                            .map { RuntimeException(it) }
                    }
                )
                .bodyToMono(object : ParameterizedTypeReference<ApiResult<Map<String, Any?>>>() {})
                .block()
            println( objectMapper.writeValueAsString(response))
        } catch(e: RuntimeException ) {
            println(e.message)
        }
    }
}