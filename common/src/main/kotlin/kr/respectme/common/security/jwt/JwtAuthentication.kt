package kr.respectme.common.security.jwt

import org.springframework.security.core.GrantedAuthority
import java.security.Principal
import java.util.UUID

data class JwtAuthentication(
    val id: UUID,
    val email: String,
    val roles: MutableCollection<out GrantedAuthority>,
    val isActivated: Boolean
): Principal {

    override fun getName(): String {
        return email
    }
}