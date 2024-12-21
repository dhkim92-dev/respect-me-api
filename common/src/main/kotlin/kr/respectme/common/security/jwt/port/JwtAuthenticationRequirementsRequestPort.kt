package kr.respectme.common.security.jwt.port

import kr.respectme.common.response.ApiResult
import kr.respectme.common.security.jwt.adapter.dto.JwtAuthenticationRequirements

interface JwtAuthenticationRequirementsRequestPort {

    fun request(authentication: String): ApiResult<JwtAuthenticationRequirements>
}