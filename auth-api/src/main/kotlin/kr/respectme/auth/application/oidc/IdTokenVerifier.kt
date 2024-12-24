package kr.respectme.auth.application.oidc

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import kr.respectme.auth.common.AuthenticationErrorCode
import kr.respectme.auth.common.AuthenticationErrorCode.OIDC_ID_TOKEN_VERIFICATION_FAILED
import kr.respectme.auth.application.oidc.idToken.AppleOidcIdTokenPayload
import kr.respectme.auth.application.oidc.idToken.CommonOidcIdTokenPayload
import kr.respectme.auth.application.oidc.idToken.GoogleOidcIdTokenPayload
import kr.respectme.common.error.BadRequestException
import kr.respectme.common.error.UnauthorizedException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.security.interfaces.RSAPublicKey

@Component
class IdTokenVerifier(
    @Qualifier("google_jwks_provider")
    private val googleJwksProvider: JwksProvider,
    @Qualifier("apple_jwks_provider")
    private val appleJwksProvider: JwksProvider,
) {

    private val GOOGLE_ISSUER_STRING = "https://accounts.google.com"
    private val APPLE_ISSUER_STRING = "https://appleid.apple.com"

    fun verifyToken(idToken: String): CommonOidcIdTokenPayload {
        val decodedJWT = JWT.decode(idToken)
        val pubKey = getPublicKey(decodedJWT)
        val algorithm = getAlgorithm(decodedJWT, pubKey)
        val verifier = JWT.require(algorithm)
            .withIssuer(decodedJWT.issuer)
            .build()

        return try {
            val verifiedJWT = verifier.verify(idToken)
            createOidcIdTokenPayload(verifiedJWT)
        } catch(e: JWTVerificationException) {
            throw UnauthorizedException(code = OIDC_ID_TOKEN_VERIFICATION_FAILED.code, message= e.message ?: OIDC_ID_TOKEN_VERIFICATION_FAILED.message)
        }
    }

    private fun createOidcIdTokenPayload(decodedJWT: DecodedJWT): CommonOidcIdTokenPayload {
        return when(decodedJWT.issuer) {
            GOOGLE_ISSUER_STRING -> CommonOidcIdTokenPayload.valueOf(googlePayload = GoogleOidcIdTokenPayload.valueOf(decodedJWT))
            APPLE_ISSUER_STRING -> CommonOidcIdTokenPayload.valueOf(applePayload = AppleOidcIdTokenPayload.valueOf(decodedJWT))
            else -> throw BadRequestException(AuthenticationErrorCode.NOT_SUPPORTED_OIDC_PROVIDER)
        }
    }

    private fun getPublicKey(decodedJWT: DecodedJWT): RSAPublicKey {
        val kid = decodedJWT.keyId
        return when(decodedJWT.issuer) {
            GOOGLE_ISSUER_STRING -> googleJwksProvider.getPublicKey(kid)
            APPLE_ISSUER_STRING -> appleJwksProvider.getPublicKey(kid)
            else -> throw BadRequestException(AuthenticationErrorCode.NOT_SUPPORTED_OIDC_PROVIDER)
        }
    }

    private fun getAlgorithm(decodedJWT: DecodedJWT, rsaPublicKey: RSAPublicKey): Algorithm {

        when(decodedJWT.algorithm) {
            "RS256" -> return Algorithm.RSA256(rsaPublicKey)
            "RS384" -> return Algorithm.RSA384(rsaPublicKey)
            "RS512" -> return Algorithm.RSA512(rsaPublicKey)
            else -> throw BadRequestException(AuthenticationErrorCode.NOT_SUPPORTED_OIDC_PROVIDER)
        }
    }

}