package kr.respectme.auth.application.oidc

data class JWKKey(
    val kty: String,
    val use: String,
    val kid: String,
    val alg: String,
    val n: String,
    val e: String
) {

}

data class JWKList(
    val keys: List<JWKKey>
) {

}