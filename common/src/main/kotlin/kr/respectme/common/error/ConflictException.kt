package kr.respectme.common.error

import org.springframework.http.HttpStatus

class ConflictException(
    override val code: String,
    override val message: String
): BusinessException(HttpStatus.CONFLICT, code, message) {

    constructor(errorCode: ErrorCode): this(message = errorCode.message, code = errorCode.code)
}