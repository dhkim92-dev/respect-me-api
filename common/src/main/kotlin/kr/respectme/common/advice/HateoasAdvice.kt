package kr.respectme.common.advice

import kr.respectme.common.advice.hateoas.Hateoas
import kr.respectme.common.advice.hateoas.HateoasResponse
import org.springframework.context.ApplicationContext
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

@ControllerAdvice
class HateoasAdvice(private val applicationContext: ApplicationContext)
    : ResponseBodyAdvice<Any?> {

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        return processRecursively(body)
    }

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    private fun processRecursively(obj: Any?): Any? {
        if ( obj == null ) return obj

        if ( obj is Collection<*> ) {
//            println("obj is collection")

            obj.forEach { processRecursively(it) }
            return obj
        }

        val clazz = obj::class

        val annotation = clazz.findAnnotation<Hateoas>()
            ?: return obj

        if ( obj !is HateoasResponse ) return obj
        val converterClass = annotation.converter
        val converter = applicationContext.getBean(converterClass.java)
        converter.convert(obj)

        clazz.memberProperties.forEach { prop ->
            try {
                prop.isAccessible = true
                val value = prop.getter.call(obj)
                processRecursively(value)
            } catch ( e: Exception ) {

            }
        }
        return obj
    }

}