package kr.respectme.common.security.jwt.adapter

import kr.respectme.common.response.ApiResult
import kr.respectme.common.security.jwt.adapter.dto.JwtValidateRequest
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PostMapping


@FeignClient(name = "respect-me-jwt-client", url = "\${respect-me.msa.auth-api.url}")
interface RestJwtAuthenticationAdapter: JwtAuthenticationPort {

    @PostMapping(value=["/api/v1/auth/jwt/verify"], consumes = ["application/json"], produces = ["application/json"])
    override fun validate(jwtValidationRequest: JwtValidateRequest): ApiResult<Map<String, Any?>>
}