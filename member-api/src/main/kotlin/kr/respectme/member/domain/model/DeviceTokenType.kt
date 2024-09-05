package kr.respectme.member.domain.model

enum class DeviceTokenType(val value: String, val dbValue: Int) {
    TYPE_FCM("FCM", 1),
    TYPE_APN("APN", 2),
    TYPE_GCM("GCM", 3),
}