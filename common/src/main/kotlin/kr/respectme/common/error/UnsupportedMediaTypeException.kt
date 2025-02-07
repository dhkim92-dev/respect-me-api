package kr.respectme.common.error

import org.springframework.http.HttpStatus

class UnsupportedMediaTypeException(
    override val code: String,
    override val message: String
): BusinessException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, code, message) {

    constructor(errorCode: ErrorCode): this(message = errorCode.message, code = errorCode.code)
}