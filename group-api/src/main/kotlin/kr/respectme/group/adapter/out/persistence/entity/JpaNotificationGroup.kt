package kr.respectme.group.adapter.out.persistence.entity

import jakarta.persistence.*
import kr.respectme.group.adapter.out.persistence.entity.converter.GroupTypeConverter
import kr.respectme.group.domain.GroupType
import java.util.*

/**
 * Group Jpa Entity
 * @property name name of group
 * @property description group description
 * @property ownerId UUID
 * @property password password to join this group, nullable
 * @property members MutableSet<JpaGroupMember>
 *     @see JpaGroupMember
 * @property notifications MutableSet<JpaGroupNotification>
 *     @see JpaGroupNotification
 * @property type type of group
 *     @see GroupType
 */
@Entity(name = "notification_group")
class JpaNotificationGroup(
    id: UUID?= null,
    description: String = "",
    name: String = "",
    password: String? = null,
    type: GroupType = GroupType.GROUP_PRIVATE,
    ownerId: UUID = UUID.randomUUID()
): BaseEntity(id) {

    @Column(length = 64)
    var name: String = name
    @Column(length = 512)
    var description : String = description
    @Convert(converter = GroupTypeConverter::class)
    var type: GroupType = type
    @Column
    var ownerId: UUID = ownerId
    @Column(nullable = true)
    var password: String? = password
}
