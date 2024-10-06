package kr.respectme.member.applications.adapter.command.strategy

interface DeviceTokenValidationStrategy {
    fun validate(token: String): Boolean
}