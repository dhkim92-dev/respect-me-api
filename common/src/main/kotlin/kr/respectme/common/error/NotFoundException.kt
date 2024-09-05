package kr.respectme.common.error

import org.springframework.http.HttpStatus

class NotFoundException(
    override val code: String,
    override val message: String
): BusinessException(HttpStatus.NOT_FOUND, code, message) {

    constructor(errorCode: ErrorCode): this(message = errorCode.message, code = errorCode.code)
}