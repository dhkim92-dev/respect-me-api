package kr.respectme.member.domain.model

import kr.respectme.common.error.BadRequestException
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.util.UUID

class Member(
    val id: UUID,
    email: String = "",
    nickname: String = "",
    password: String = "",
    role: MemberRole = MemberRole.ROLE_MEMBER,
    isBlocked: Boolean = false,
    blockReason: String = "",
    val createdAt: Instant = Instant.now(),
    deviceTokens: MutableSet<DeviceToken> = mutableSetOf()
) {
    var email: String = email
        private set

    var nickname: String = nickname
        private set

    var password: String = password
        private set

    var role: MemberRole = role
        private set

    var isBlocked: Boolean = isBlocked
        private set

    var blockReason: String = blockReason
        private set

    private val deviceTokens: MutableSet<DeviceToken> = deviceTokens

    fun registerDeviceToken(token: DeviceToken) {
        if(token.memberId != this.id) { throw IllegalStateException("Not your device token.") }
        deviceTokens.add(token)
    }

    fun removeDeviceToken(token: DeviceToken) {
        deviceTokens.remove(token)
    }

    fun changePassword(encoder: PasswordEncoder, password: String?) {
        password?.let {
            this.password = encoder.encode(password)
        }
    }

    fun changeNickname(value: String?) {
        value?.let{
            nickname = value
        }
    }

    fun block(reason: String) {
        isBlocked = true
        blockReason = reason
    }

    fun unblock() {
        isBlocked = false
        blockReason = ""
    }
}