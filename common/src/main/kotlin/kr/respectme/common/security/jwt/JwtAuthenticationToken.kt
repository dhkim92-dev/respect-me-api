package kr.respectme.common.security.jwt

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthenticationToken(private var _principal: Any): AbstractAuthenticationToken(null) {

    override fun getName(): String {
        return (_principal as JwtAuthentication).email
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return (_principal as JwtAuthentication).roles
    }

    override fun getDetails(): Any {
        return (_principal as JwtAuthentication).id
    }

    override fun isAuthenticated(): Boolean {
        return !(_principal as JwtAuthentication).isActivated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {

    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return _principal
    }
}