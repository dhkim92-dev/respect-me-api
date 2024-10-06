package kr.respectme.member.domain.model

enum class DeviceTokenType(val dbValue: Int) {
    FCM(1),
    APN(2),
    GCM( 3),
}