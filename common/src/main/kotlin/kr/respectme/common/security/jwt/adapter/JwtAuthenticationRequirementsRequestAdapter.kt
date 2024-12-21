package kr.respectme.common.security.jwt.adapter

import kr.respectme.common.response.ApiResult
import kr.respectme.common.security.jwt.adapter.dto.JwtAuthenticationRequirements
import kr.respectme.common.security.jwt.port.JwtAuthenticationRequirementsRequestPort
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name="respect-me-auth-service", url="\${respect-me.security.jwt.auth-api.url}")
interface JwtAuthenticationRequirementsRequestAdapter: JwtAuthenticationRequirementsRequestPort {

    @GetMapping("/api/v1/auth/jwt/verification/requirements", produces=["application/json"], consumes = ["application/json"])
    override fun request(@RequestHeader("Authorization") authentication: String): ApiResult<JwtAuthenticationRequirements>
}