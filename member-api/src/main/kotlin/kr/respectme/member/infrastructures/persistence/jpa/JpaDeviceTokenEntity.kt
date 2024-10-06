package kr.respectme.member.infrastructures.persistence.jpa

import jakarta.persistence.*
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.member.common.persistent.CreatedAtUpdatedAtEntity
import kr.respectme.member.domain.model.DeviceTokenType
import java.time.Instant
import java.util.UUID

@Entity(name="device_token")
class JpaDeviceTokenEntity(
    @Id
    val id: UUID = UUIDV7Generator.generate(),
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: JpaMemberEntity = JpaMemberEntity(UUID.randomUUID()),
    @Convert(converter = DeviceTokenTypeConverter::class)
    var type: DeviceTokenType = DeviceTokenType.FCM,
    @Column
    var token: String="",
    @Column
    var lastUsedAt: Instant = Instant.now(),
    @Column
    var isActivated: Boolean = true
): CreatedAtUpdatedAtEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JpaDeviceTokenEntity) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}