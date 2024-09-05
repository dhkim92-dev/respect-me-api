package kr.respectme.common.error

import org.springframework.http.HttpStatus

class ForbiddenException(
    override val code: String,
    override val message: String
): BusinessException(status = HttpStatus.FORBIDDEN, code = code, message = message) {

    constructor(errorCode: ErrorCode): this(message = errorCode.message, code = errorCode.code)
}