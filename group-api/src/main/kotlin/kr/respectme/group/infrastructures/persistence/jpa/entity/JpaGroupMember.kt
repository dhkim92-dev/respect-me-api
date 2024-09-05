package kr.respectme.group.infrastructures.persistence.jpa.entity

import jakarta.persistence.*
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.group.common.persistent.CreatedAtUpdatedAtEntity
import kr.respectme.group.domain.GroupMemberRole
import kr.respectme.group.infrastructures.persistence.jpa.entity.converter.GroupMemberRoleConverter
import kr.respectme.group.infrastructures.persistence.jpa.entity.notifications.JpaGroupNotification
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
    pk: Pk = Pk(UUID.randomUUID(), UUID.randomUUID()),
    nickname: String = "",
    @Convert(converter = GroupMemberRoleConverter::class)
    var memberRole: GroupMemberRole = GroupMemberRole.MEMBER,
    profileImageUrl: String? = null,
    @MapsId("groupId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    val group: JpaNotificationGroup = JpaNotificationGroup(id=pk.groupId),
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val notifications: MutableSet<JpaGroupNotification> = mutableSetOf()
): CreatedAtUpdatedAtEntity() {


    @EmbeddedId
    val pk: Pk = Pk(groupId = pk.groupId, memberId = pk.memberId)

    @Column
    var nickname: String = nickname

    @Column
    var profileImageUrl: String? = profileImageUrl

    @Embeddable
    class Pk(
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