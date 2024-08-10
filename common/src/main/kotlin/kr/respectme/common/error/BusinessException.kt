package kr.respectme.common.error

import org.springframework.http.HttpStatus

open class BusinessException(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
): RuntimeException(message), ErrorCode {

}