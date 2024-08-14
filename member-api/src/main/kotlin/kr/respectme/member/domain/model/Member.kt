package kr.respectme.member.domain.model

import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import java.util.UUID

class Member(
    val id: UUID,
    var email: String,
    var nickname: String,
    var password: String,
    var role: MemberRole,
    var isBlocked: Boolean,
    var blockReason: String,
    var createdAt: LocalDateTime
) {

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