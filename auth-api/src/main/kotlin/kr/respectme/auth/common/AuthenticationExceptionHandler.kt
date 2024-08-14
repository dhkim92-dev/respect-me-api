package kr.respectme.auth.common

import kr.respectme.common.advice.GeneralExceptionHandlerAdvice
import org.springframework.web.bind.annotation.ControllerAdvice

@ControllerAdvice
class AuthenticationExceptionHandler: GeneralExceptionHandlerAdvice() {

}