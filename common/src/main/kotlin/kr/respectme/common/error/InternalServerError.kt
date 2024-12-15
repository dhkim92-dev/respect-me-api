package kr.respectme.common.error

import org.springframework.http.HttpStatus

class InternalServerError(message: String): BusinessException(
    status = HttpStatus.INTERNAL_SERVER_ERROR,
    code = "INTERNAL_SERVER_ERROR",
    message = message
){
}