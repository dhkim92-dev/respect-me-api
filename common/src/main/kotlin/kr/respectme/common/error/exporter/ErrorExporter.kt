package kr.respectme.common.error.exporter

import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper

interface ErrorExporter {

//    fun exportMessage(message: String): Unit

    fun export(request: ContentCachingRequestWrapper, response: ContentCachingResponseWrapper)
}