package kr.respectme.common.error

import org.springframework.http.HttpStatus

class UnauthorizedException(
    override val code: String,
    override val message: String
): BusinessException(HttpStatus.UNAUTHORIZED, code, message) {

    constructor(errorCode: ErrorCode): this(message = errorCode.message, code = errorCode.code)
}