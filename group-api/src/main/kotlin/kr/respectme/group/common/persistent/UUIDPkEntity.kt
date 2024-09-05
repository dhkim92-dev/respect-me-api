package kr.respectme.group.common.persistent

import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PostPersist
import kr.respectme.common.utility.UUIDV7Generator
import org.hibernate.proxy.HibernateProxy
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import java.util.Objects
import java.util.UUID

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class UUIDPkEntity(id: UUID?=null): CreatedAtUpdatedAtEntity(), Persistable<UUID> {

    @Id
    private val id: UUID = id ?: UUIDV7Generator.generate()

    @Transient
    private var _isNew: Boolean = true

    override fun getId(): UUID {
        return id
    }

    override fun isNew(): Boolean {
        return _isNew
    }

    private fun getIdentifier(obj: Any): Serializable {
        return if(obj is HibernateProxy) {
            obj.hibernateLazyInitializer.identifier as Serializable
        } else {
            (obj as UUIDPkEntity).id
        }
    }

    @PostPersist
    @PostLoad
    protected fun markNotNew() {
        _isNew = false
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other !is HibernateProxy && this::class != other::class) return false

        return id == getIdentifier(other)
    }

    override fun hashCode(): Int {
        return Objects.hashCode(id)
    }
}