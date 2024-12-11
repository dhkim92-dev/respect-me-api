package kr.respectme.group.adapter.out.persistence.entity

import jakarta.persistence.*
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.adapter.out.persistence.entity.converter.GroupMemberRoleConverter
import kr.respectme.group.domain.GroupMemberRole
import java.io.Serializable
import java.util.*

/**
 * JpaGroupMember Entity
 * @field pk Composite primary key of the entity, groupId and memberId required.
 * @field nickname the member's nickname want to use in the group
 * @field memberRole role of the member in the group
 */
@Entity(name = "notification_group_member")
class JpaGroupMember(
    pk: Pk? = null,
    nickname: String = "",
    @Convert(converter = GroupMemberRoleConverter::class)
    var memberRole: GroupMemberRole = GroupMemberRole.MEMBER,
    profileImageUrl: String? = null,
): CreatedAtUpdatedAtEntity() {

    @EmbeddedId
    val pk: Pk? = pk

    @Column
    var nickname: String = nickname

    @Column
    var profileImageUrl: String? = profileImageUrl

    @Embeddable
    class Pk(
        @Column(name="group_id")
        var groupId: UUID = UUIDV7Generator.generate(),
        @Column(name="member_id")
        var memberId: UUID = UUIDV7Generator.generate()
    ): Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is Pk) return false
            return groupId == other.groupId && memberId == other.memberId
        }

        override fun hashCode(): Int {
            return Objects.hash(groupId, memberId)
        }
    }
}