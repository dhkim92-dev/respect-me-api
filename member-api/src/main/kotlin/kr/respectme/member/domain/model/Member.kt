package kr.respectme.member.domain.model

import kr.respectme.common.domain.BaseDomainEntity
import kr.respectme.common.domain.annotations.DomainEntity
import kr.respectme.common.domain.annotations.DomainRelation
import kr.respectme.common.error.BadRequestException
import kr.respectme.member.common.code.MemberServiceErrorCode
import java.time.Instant
import java.util.UUID

@DomainEntity
class Member(
    id : UUID,
    private var email: String = "",
    private var role: MemberRole = MemberRole.ROLE_MEMBER,
    private var isBlocked: Boolean = false,
    private var blockReason: String = "",
    val createdAt: Instant = Instant.now(),
    @DomainRelation
    private var deviceTokens: MutableSet<DeviceToken> = mutableSetOf(),
    private var isDeleted: Boolean = false
): BaseDomainEntity<UUID>(id) {

    fun getEmail(): String {
        return email
    }

    fun getRole(): MemberRole {
        return role
    }

    fun getIsBlocked(): Boolean {
        return isBlocked
    }

    fun getBlockReason(): String {
        return blockReason
    }

    fun getDeviceTokens(): Set<DeviceToken> {
        return deviceTokens
    }

    fun getIsDeleted(): Boolean {
        return isDeleted
    }

    fun registerDeviceToken(token: DeviceToken) {
        if(token.getMemberId() != this.id) {
            println("token member id : ${token.getMemberId()} this.id : ${id}")
            throw IllegalStateException("Not your device token.")
        }

        if( !deviceTokens.contains(token)  && deviceTokens.size > 3) {
            throw BadRequestException(MemberServiceErrorCode.EXCEEDED_DEVICE_TOKEN_LIMIT)
        }
        token.useToken()
        deviceTokens.add(token)
    }

    fun removeDeviceToken(token: DeviceToken?): Boolean {
        token?.let {
            return deviceTokens.remove(token)
        }

        return true
    }

    fun block(reason: String) {
        isBlocked = true
        blockReason = reason
    }

    fun unblock() {
        isBlocked = false
        blockReason = ""
    }

    fun setSoftDeleted(value: Boolean) {
        isDeleted = value
    }
}