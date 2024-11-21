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
    private val objectMapper: ObjectMapper
): AuthenticationProvider{

    private val logger = LoggerFactory.getLogger(javaClass)
    private val checkFields = listOf("subject", "nickname", "email", "roles", "activated")

    init {
        logger.debug("JwtAuthenticationProvider generated.")
    }

    override fun authenticate(authentication: Authentication?): Authentication? {
        logger.debug("JwtAuthenticationProvider.authenticate")
        if(authentication == null) return null
        val request = JwtValidateRequest("Bearer", authentication.principal as String)
        logger.info("JwtAuthenticaitonProvider request body : ${request}")
        val response = jwtAuthenticationPort.validate(request)
        logger.info("Jwt Authentication Response : ${objectMapper.writeValueAsString(response)}")
//        val jwtAuthentication = objectMapper.convertValue(response, JwtAuthentication::class.java)
        val jwtAuthentication = convertToJwtAuthentication(response.data)
        logger.debug("${jwtAuthentication}")
        return JwtAuthenticationToken(jwtAuthentication)
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return (JwtAuthenticationToken::class.java.isAssignableFrom(authentication))
    }

    private fun convertToJwtAuthentication(response: Map<String, Any?>): JwtAuthentication {

        checkFields.forEach { key -> if(!response.containsKey(key)) {
            throw UnauthorizedException(GlobalErrorCode.JWT_PAYLOAD_VERSION_MISMATCH)
        }}

        return JwtAuthentication(
            id = UUID.fromString(response["subject"] as String),
            nickname = response["nickname"] as String,
            email = response["email"] as String,
            roles = (response["roles"] as List<String>).map{ it -> SimpleGrantedAuthority(it) }.toMutableList(),
            isActivated = response["activated"] as Boolean
        )
    }
}