package kr.respectme.common.security.jwt.port

import kr.respectme.common.response.ApiResult
import kr.respectme.common.security.jwt.JwtClaims
import kr.respectme.common.security.jwt.adapter.dto.JwtValidateRequest

interface JwtAuthenticationPort {

    fun verify(jwtValidationRequest: JwtValidateRequest): JwtClaims
}