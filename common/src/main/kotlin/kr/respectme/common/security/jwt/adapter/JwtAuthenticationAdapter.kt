package kr.respectme.common.security.jwt.adapter

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import kr.respectme.common.security.jwt.JwtClaims
import kr.respectme.common.security.jwt.adapter.dto.JwtValidateRequest
import kr.respectme.common.security.jwt.port.JwtAuthenticationPort
import kr.respectme.common.security.jwt.adapter.dto.JwtAuthenticationRequirements
import kr.respectme.common.security.jwt.port.JwtAuthenticationRequirementsRequestPort
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class JwtAuthenticationAdapter(
    private val jwtAuthenticationRequirementsRequestPort: JwtAuthenticationRequirementsRequestPort,
    private val serviceToken: String,
): JwtAuthenticationPort {

    private val cacheTTL = TimeUnit.HOURS.toMillis(12L)

    private var cacheExpiryTime: Long = 0L

    private var jwtAuthenticationRequirements: JwtAuthenticationRequirements? = null

    private lateinit var accessTokenVerifier: JWTVerifier

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        logger.info("JwtAuthenticationAdapter generated.")
        try {
            jwtAuthenticationRequirements = getRequirements()
            accessTokenVerifier = JWT.require(getAlgorithm())
                .withIssuer(jwtAuthenticationRequirements!!.issuer)
                .build()
        }catch(e: Exception) {
            logger.error("JWT Authentication Adapter Initialization Failed : ${e.message}")
        }
    }

    override fun verify(jwtValidationRequest: JwtValidateRequest): JwtClaims {
        if(jwtAuthenticationRequirements==null || System.currentTimeMillis() > cacheExpiryTime) {
            updateVerifier()
        }

        return try {
            val decodedJWT = accessTokenVerifier.verify(jwtValidationRequest.accessToken)
            JwtClaims.valueOf(decodedJWT)
        } catch (e: Exception) {
            logger.error("JWT Verification Failed : ${e.message}")
            throw e
        }
    }

    @Synchronized
    private fun updateVerifier() {
        jwtAuthenticationRequirements = getRequirements()
        accessTokenVerifier = JWT.require(getAlgorithm())
            .withIssuer(jwtAuthenticationRequirements!!.issuer)
            .build()
    }

    private fun getRequirements(): JwtAuthenticationRequirements {
        val requirements = jwtAuthenticationRequirementsRequestPort.request(serviceToken)
        cacheExpiryTime = System.currentTimeMillis() + cacheTTL
        return requirements.data
    }

    private fun getAlgorithm(): Algorithm {
        return Algorithm.HMAC256(jwtAuthenticationRequirements!!.secret)
    }
}