package kr.respectme.common.advice

import kr.respectme.common.annotation.CursorPagination
import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.utility.PaginationUtility
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class CursorPaginationAdvice: ResponseBodyAdvice<Any?> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        logger.debug("cursor pagination advice beforeBodyWrite")
        val servletRequestAttributes = RequestContextHolder.getRequestAttributes()
                as? ServletRequestAttributes
        val handler = servletRequestAttributes?.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler", 0)
                as? HandlerMethod
        val params = handler?.methodParameters?.filter { it.hasParameterAnnotation(CursorParam::class.java) }
        logger.debug("cursor pagination param : ${params.toString()}")
        val queryMap = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes)
            .request
            .parameterMap
        logger.debug(params?.map{it.parameterName}.toString())

        logger.debug("body : ${body.toString()}")
        return if (body is List<*>) {
            logger.debug("cursor pagination advice beforeBodyWrite body is list")
            params?.let { PaginationUtility.toCursorList(body, params.toList(), queryMap) }
                ?: body
        } else {
            logger.debug("cursor pagination advice beforeBodyWrite body is not list")
            body
        }
    }

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return returnType.hasMethodAnnotation(CursorPagination::class.java)
    } }