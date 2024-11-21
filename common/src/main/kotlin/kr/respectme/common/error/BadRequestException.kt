package kr.respectme.common.error

import org.springframework.http.HttpStatus

class BadRequestException(
    override val code: String,
    override val message: String
): BusinessException(HttpStatus.BAD_REQUEST, code, message) {

    constructor(errorCode: ErrorCode): this(message = errorCode.message, code = errorCode.code)
}