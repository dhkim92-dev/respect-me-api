package kr.respectme.common.utility

import kr.respectme.common.annotation.CursorParam
import kr.respectme.common.response.CursorList
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.util.UriComponentsBuilder
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class PaginationUtility {


    companion object {

        private val logger = LoggerFactory.getLogger(javaClass)

        private fun getRequestUrl(): String {
            val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
            val url = request.requestURL.toString()
            logger.debug("string url : ${url}")

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
            val paramMap = parameters.filter { param -> param.hasParameterAnnotation(CursorParam::class.java) }
                .map{param ->  param.parameterName!! to param.getParameterAnnotation(CursorParam::class.java)!! }
                .toMap()
            val lastElement = data.lastOrNull()
            val url = getRequestUrl()
            logger.debug("getRequestUrl : ${url}")
//            val uriBuilder = UriComponentsBuilder.fromOriginHeader(getRequestUrl())
            val uriBuilder = UriComponentsBuilder.fromHttpUrl(url)

            for(param in paramMap) {
                val queryKey = param.key
                val path = param.value.key
                logger.debug("queryKey : ${queryKey} path : ${path}")
                if(param.value.inherit) {
                    uriBuilder.queryParam(queryKey, queryMap[queryKey])
                }
                getNestedField(lastElement, path)?.let {obj ->
                    uriBuilder.queryParam(queryKey, obj.toString())
                }
            }

            val pageSize = queryMap["size"]
                ?.takeIf { it.isNotEmpty() }
                ?.firstOrNull()
                ?.toIntOrNull()
                ?: 20

            val uri = uriBuilder.queryParam("size", pageSize).toUriString()
            logger.debug("""uri : ${uri}""")

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