package kr.respectme.common.error

import org.springframework.http.HttpStatus

interface ErrorCode {
    val code: String
    val status: HttpStatus
    val message: String
}