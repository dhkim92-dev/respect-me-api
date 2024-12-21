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
    val memberId: MemberId? = null,
    @Column
    val email: String = "",
    @Column
    val password: String? = null,

    @AttributeOverrides(
        AttributeOverride(name = "platform", column = Column(name = "oidc_auth_platform")),
        AttributeOverride(name = "userIdentifier", column = Column(name = "oidc_auth_user_identifier", nullable = true))
    )
    val oidcAuth: OidcAuth = OidcAuth(),
    lastLoginAt: Instant? = null
){
    @Column
    var lastLoginAt: Instant? = lastLoginAt
    protected set

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
}