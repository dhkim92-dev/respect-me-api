package kr.respectme.common.advice

import kr.respectme.common.annotation.ApplicationResponse
import kr.respectme.common.response.ApiResult
import kr.respectme.common.response.CursorList
import kr.respectme.common.response.ResponseCode
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.core.MethodParameter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@Order(Ordered.LOWEST_PRECEDENCE)
@ControllerAdvice
class EnvelopPatternResponseBodyAdvice: ResponseBodyAdvice<Any> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        // supports를 통과했기 때문에 무조건 ApplicationResponse가 존재함
        val applicationResponse = returnType.getMethodAnnotation(ApplicationResponse::class.java)!!
        response.setStatusCode(applicationResponse.status)

        return ApiResult<Any?>(
            status = applicationResponse.status.value(),
            message = applicationResponse.message,
            data = body,
        )
    }

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
//        return converterType.isAssignableFrom(MappingJackson2HttpMessageConverter::class.java)
        return MappingJackson2HttpMessageConverter::class.java.isAssignableFrom(converterType)
                && returnType.hasMethodAnnotation(ApplicationResponse::class.java)
    }
}