package kr.respectme.common.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.common.error.GlobalErrorCode
import kr.respectme.common.error.UnauthorizedException
import kr.respectme.common.security.jwt.adapter.dto.JwtValidateRequest
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID

class JwtAuthenticationProvider(
    private val jwtAuthenticationPort: JwtAuthenticationPort,
): AuthenticationProvider{

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.debug("JwtAuthenticationProvider generated.")
    }

    override fun authenticate(authentication: Authentication?): Authentication? {
        logger.debug("JwtAuthenticationProvider.authenticate")
        if(authentication == null) return null

        val jwtClaims = jwtAuthenticationPort.verify(JwtValidateRequest("Bearer", authentication.principal as String))
        logger.debug("JWT decode result: {}", jwtClaims)
        val jwtAuthentication = convertToJwtAuthentication(jwtClaims)
        return JwtAuthenticationToken(jwtAuthentication)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return (JwtAuthenticationToken::class.java.isAssignableFrom(authentication))
    }

    private fun convertToJwtAuthentication(jwtClaims: JwtClaims): JwtAuthentication {
        return JwtAuthentication(
            id = UUID.fromString(jwtClaims.memberId),
            email = jwtClaims.email,
            roles = jwtClaims.roles.map{ it -> SimpleGrantedAuthority(it) }.toMutableList(),
            isActivated = jwtClaims.isActivated
        )
    }
}