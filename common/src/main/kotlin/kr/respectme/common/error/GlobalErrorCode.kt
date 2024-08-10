package kr.respectme.common.error

import org.springframework.http.HttpStatus

enum class GlobalErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
): ErrorCode {

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "GE-0001", "Invalid input value"),
}