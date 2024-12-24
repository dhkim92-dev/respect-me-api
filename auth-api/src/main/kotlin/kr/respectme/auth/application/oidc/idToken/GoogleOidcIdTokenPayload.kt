package kr.respectme.auth.application.oidc.idToken

import com.auth0.jwt.interfaces.DecodedJWT

class GoogleOidcIdTokenPayload(
    val iss: String,
    val sub: String,
    val aud: String,
    val iat: Long,
    val exp: Long,
    val email: String,
    val emailVerified: Boolean,
    val name: String,
    val picture: String,
    val givenName: String,
    val familyName: String,
    val locale: String
) {

    companion object {

        fun valueOf(decodedJWT: DecodedJWT): GoogleOidcIdTokenPayload {
            return GoogleOidcIdTokenPayload(
                iss = decodedJWT.issuer,
                sub = decodedJWT.subject,
                aud = decodedJWT.audience[0],
                iat = decodedJWT.issuedAt.time / 1000,
                exp = decodedJWT.expiresAt.time / 1000,
                email = decodedJWT.claims["email"]?.asString() ?: "",
                emailVerified = decodedJWT.claims["email_verified"]?.asBoolean() ?: false,
                name = decodedJWT.claims["name"]?.asString() ?: "",
                picture = decodedJWT.claims["picture"]?.asString() ?: "",
                givenName = decodedJWT.claims["given_name"]?.asString() ?: "",
                familyName = decodedJWT.claims["family_name"]?.asString() ?: "",
                locale = decodedJWT.claims["locale"]?.asString() ?: ""
            )
        }
    }
}