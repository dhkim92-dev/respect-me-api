package kr.respectme.report.domain

import jakarta.persistence.*
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
class LongBaseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
) {

    val identifier: Long
        get() = id ?: throw IllegalStateException("Entity must be persisted to get identifier")

    var createdAt: Instant = Instant.now()

    var updatedAt: Instant? = null;

    @PrePersist
    fun prePersist() {
        createdAt = Instant.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = Instant.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LongBaseEntity) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}