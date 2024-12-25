package kr.respectme.common.security.jwt

import com.auth0.jwt.interfaces.DecodedJWT

data class JwtClaims(
    val memberId: String,
    val email: String,
    val roles: List<String>,
    val isActivated: Boolean
) {

    companion object {
        
        fun valueOf(decodedJWT: DecodedJWT): JwtClaims {
            return JwtClaims(
                memberId = decodedJWT.subject,
                email = decodedJWT.getClaim("email").asString(),
                roles = decodedJWT.getClaim("roles").asList(String::class.java),
                isActivated = decodedJWT.getClaim("isActivated").asBoolean()
            )
        }
    }
}