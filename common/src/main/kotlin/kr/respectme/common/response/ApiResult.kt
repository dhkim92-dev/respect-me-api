package kr.respectme.common.response

import org.slf4j.MDC

class ApiResult<T>(
    var traceId: String? = MDC.get("traceId"),
    val status: Int = 200,
    val data: T,
    val message: String? = null
) {

}