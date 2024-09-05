package kr.respectme.auth.common

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.advice.GeneralExceptionHandlerAdvice
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class AuthenticationExceptionHandler(
    private val objectMapper: ObjectMapper
): GeneralExceptionHandlerAdvice(objectMapper = objectMapper) {

}