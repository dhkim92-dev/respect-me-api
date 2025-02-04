package kr.respectme.member.applications.strategy

interface DeviceTokenValidationStrategy {
    fun validate(token: String): Boolean
}