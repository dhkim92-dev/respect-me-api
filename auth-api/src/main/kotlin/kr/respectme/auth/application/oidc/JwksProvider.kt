package kr.respectme.auth.application.oidc

import java.security.interfaces.RSAPublicKey

interface JwksProvider {

    fun getPublicKey(kid: String): RSAPublicKey
}