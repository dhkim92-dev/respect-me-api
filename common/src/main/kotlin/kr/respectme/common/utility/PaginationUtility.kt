package kr.respectme.common.utility

import jakarta.servlet.http.HttpServletRequest
import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.response.CursorList
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.UriComponentsBuilder
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class PaginationUtility {

    companion object {

        private fun getHost(): String {
            val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
            val url = request.requestURL.toString()

            return when {
                url.startsWith("https:") -> url
                url.startsWith("http:") && !url.contains("localhost") && !url.contains("127.0.0.1")
                    -> url.replace("http:", "https:")
                else -> url
            }
        }

        fun toCursorList(
            data: List<Any?>,
            parameters: List<MethodParameter>,
            queryMap: Map<String, Array<String>>
        ): CursorList<Any?> {
            val paramMap = parameters.filter { param -> param.hasMethodAnnotation(CursorParam::class.java) }
                .map{param ->  param.parameterName!! to param.getParameterAnnotation(CursorParam::class.java)!! }
                .toMap()
            val lastElement = data.lastOrNull()
            val uriBuilder = UriComponentsBuilder.fromOriginHeader(getHost())

            for(param in paramMap) {
                val queryKey = param.key
                val path = param.value.key
                if(param.value.inherit) {
                    uriBuilder.queryParam(queryKey, queryMap[queryKey])
                }
                getNestedField(lastElement, path)?.let {obj ->
                    uriBuilder.queryParam(queryKey, obj.toString())
                }
            }
            val uri = uriBuilder.toUriString()
            val pageSize = queryMap["size"]
                ?.takeIf { it.isNotEmpty() }
                ?.firstOrNull()
                ?.toIntOrNull()
                ?: 40

            return CursorList.of(data, uri, pageSize)
        }

        fun getNestedField(obj: Any?, path: String): Any? {
            val keys = path.split(".")
            var currentObj: Any? = obj

            for(key in keys) {
                if(currentObj == null) return null
                val properties = currentObj::class.memberProperties
                val property = properties.find { it.name == key }
                        as? KProperty1<Any, *>
                    ?: return null
                property.isAccessible = true
                currentObj = property.get(currentObj)
            }

            return currentObj
        }
    }
}