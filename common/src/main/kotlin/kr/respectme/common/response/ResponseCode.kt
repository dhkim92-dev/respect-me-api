package kr.respectme.common.response

import org.springframework.http.HttpStatus

interface ResponseCode {
    val status: HttpStatus
    val code: String
    val message: String?
}