package kr.respectme.auth.port.`in`.interfaces.dto

import kr.respectme.auth.application.dto.TokenValidationResult
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID

data class VerifyAccessTokenResponse(
    val type: String,
    val issuer: String,
    val subject: UUID,
    val email: String,
    val nickname: String,
    val roles: Array<String>,
    val activated: Boolean,
) {

    companion object {

        fun of(result: TokenValidationResult): VerifyAccessTokenResponse {
            return VerifyAccessTokenResponse(
                type = result.type,
                issuer = result.issuer,
                subject = result.memberId,
                nickname = result.nickname,
                email = result.email,
                roles = result.roles,
                activated = result.isActivated
            )
        }
    }
}