package kr.respectme.common.response

import org.springframework.http.HttpStatus

class ApiResult<T>(
    val traceId: String?=null,
    val status: Int = 200,
    val data: T,
    val message: String? = null
) {

}