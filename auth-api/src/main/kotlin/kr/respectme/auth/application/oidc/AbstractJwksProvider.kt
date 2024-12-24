package kr.respectme.auth.application.oidc

import com.fasterxml.jackson.databind.ObjectMapper
import kr.respectme.auth.common.AuthenticationErrorCode
import kr.respectme.auth.common.AuthenticationErrorCode.OIDC_FETCH_JWKS_FAILED
import kr.respectme.common.error.UnauthorizedException
import org.springframework.web.reactive.function.client.WebClient
import java.math.BigInteger
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.RSAPublicKeySpec
import java.util.*
import java.util.concurrent.TimeUnit

abstract class AbstractJwksProvider(
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper
): JwksProvider {

    abstract val jwksUrl: String

    private var cachedJwks: List<JWKKey> = emptyList()

    private var cacheExpiryTime: Long = 0L

    private val cacheTTL = TimeUnit.HOURS.toMillis(1)

    private val lock = Any()

    override fun getPublicKey(kid: String): RSAPublicKey {
        val jwtKey = fetchJwks(kid).firstOrNull { it.kid == kid }
            ?: throw UnauthorizedException(AuthenticationErrorCode.OIDC_FETCH_JWKS_FAILED)

        return createPublicKey(jwtKey)
    }

    private fun createPublicKey(jwkKey: JWKKey): RSAPublicKey {
        val modulus = jwkKey.n
        val exponent = jwkKey.e
        val modulusBytes = Base64.getUrlDecoder().decode(modulus)
        val exponentBytes = Base64.getUrlDecoder().decode(exponent)
        val rsaPublicKeySpec = RSAPublicKeySpec(BigInteger(1, modulusBytes), BigInteger(1, exponentBytes))
        return KeyFactory.getInstance("RSA").generatePublic(rsaPublicKeySpec) as RSAPublicKey
    }

    private fun fetchJwks(kid: String): List<JWKKey> {
        if (!cachedJwks.isEmpty() && System.currentTimeMillis() < cacheExpiryTime) {
            return cachedJwks
        }

        synchronized(lock) {
            if (!cachedJwks.isEmpty() && System.currentTimeMillis() < cacheExpiryTime) {
                return cachedJwks
            }

            return webClient.get()
                .uri(jwksUrl)
                .retrieve()
                .bodyToMono(JWKList::class.java)
                .block()
                ?.keys.also {
                    cachedJwks = it ?: emptyList()
                    cacheExpiryTime = System.currentTimeMillis() + cacheTTL
                }
                ?: throw UnauthorizedException(OIDC_FETCH_JWKS_FAILED)
        }
    }
}