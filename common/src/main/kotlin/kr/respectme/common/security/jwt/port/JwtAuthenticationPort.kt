package kr.respectme.common.security.jwt.port

import kr.respectme.common.response.ApiResult
import kr.respectme.common.security.jwt.adapter.dto.JwtValidateRequest

interface JwtAuthenticationPort {

    fun validate(jwtValidationRequest: JwtValidateRequest): ApiResult<Map<String, Any?>>
}