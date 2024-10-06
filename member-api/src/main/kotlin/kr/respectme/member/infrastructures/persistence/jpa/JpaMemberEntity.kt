package kr.respectme.member.infrastructures.persistence.jpa

import jakarta.persistence.*
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.member.common.persistent.CreatedAtUpdatedAtEntity
import kr.respectme.member.domain.model.MemberRole
import java.util.*

@Entity(name="member")
class JpaMemberEntity(
    @Id
    val id: UUID = UUIDV7Generator.generate(),
    @Column
    var email: String = "",
    @Column
    var nickname: String = "",
    @Column
    var password: String = "",
    @Convert(converter = MemberRoleConverter::class)
    var role: MemberRole = MemberRole.ROLE_MEMBER,
    @Column
    var isBlocked: Boolean = false,
    @Column
    var blockReason: String = "",
    @OneToMany(mappedBy = "member",
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        orphanRemoval = true)
    var deviceTokens: MutableSet<JpaDeviceTokenEntity> = mutableSetOf()
): CreatedAtUpdatedAtEntity() {

}