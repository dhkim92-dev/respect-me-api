package kr.respectme.auth.common.oidc

import java.security.interfaces.RSAPublicKey

interface JwksProvider {

    fun getPublicKey(kid: String): RSAPublicKey
}