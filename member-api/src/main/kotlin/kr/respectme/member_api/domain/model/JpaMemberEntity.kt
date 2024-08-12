package kr.respectme.member_api.domain.model

import jakarta.persistence.*
import kr.respectme.member_api.common.persistent.CreatedAtUpdatedAtFieldEntity
import kr.respectme.member_api.domain.model.MemberRole
import java.util.*

@Entity(name="member")
class JpaMemberEntity(
    @Id
    val id: UUID = UUID.randomUUID(),
    @Column
    var email: String = "",
    @Column
    var nickname: String = "",
    @Column
    var password: String = "",
    @Enumerated(EnumType.STRING)
    var role: MemberRole = MemberRole.ROLE_MEMBER,
    @Column
    var isBlocked: Boolean = false,
    @Column
    var blockReason: String = ""
): CreatedAtUpdatedAtFieldEntity() {

}