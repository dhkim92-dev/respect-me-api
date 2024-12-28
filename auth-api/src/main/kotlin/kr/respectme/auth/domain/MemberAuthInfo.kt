package kr.respectme.auth.domain

import jakarta.persistence.*
import java.time.Instant


@Entity
@Table(
    indexes = [
        Index(name = "idx_platform_identifier", columnList = "oidc_auth_platform, oidc_auth_user_identifier")
    ]
)
class MemberAuthInfo(
    @Id
    @Embedded
    private val memberId: MemberId? = null,
    @AttributeOverrides(
        AttributeOverride(name = "platform", column = Column(name = "oidc_auth_platform")),
        AttributeOverride(name = "userIdentifier", column = Column(name = "oidc_auth_user_identifier", nullable = true))
    )
    @Embedded
    private val oidcAuth: OidcAuth = OidcAuth(),
    @Column
    private val email: String = "",
    @Column
    private var lastLoginAt: Instant? = null,
    @Column
    private var password: String? = null,
    @Column
    private var isDeleted: Boolean = false
){
    fun getMemberId(): MemberId {
        return memberId!!
    }

    fun getEmail(): String {
        return email
    }

    fun getOidcAuth(): OidcAuth {
        return oidcAuth
    }

    fun getLastLoginAt(): Instant? {
        return lastLoginAt
    }

    fun getPassword(): String? {
        return password
    }

    fun isDeleted(): Boolean {
        return isDeleted
    }

    fun changePassword(password: String) {
        this.password = password
    }

    fun setIsDeleted(isDeleted: Boolean) {
        this.isDeleted = isDeleted
    }

    fun login() {
        lastLoginAt = Instant.now()
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false
        if(this.memberId === null && (other as MemberAuthInfo).memberId === null) return false

        return memberId == (other as MemberAuthInfo).memberId
    }

    override fun hashCode(): Int {
        return memberId.hashCode()
    }

    override fun toString(): String {
        return """{
            memberId=$memberId, 
            email='$email', 
            oidcAuth=$oidcAuth, 
            lastLoginAt=$lastLoginAt)
            }""".trimMargin()
    }
}