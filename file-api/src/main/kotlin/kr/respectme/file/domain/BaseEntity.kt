package kr.respectme.file.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant


@MappedSuperclass
@EntityListeners(value = [AuditingEntityListener::class])
abstract class BaseEntity(
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    protected val id : Long?
){
    private var createdAt: Instant = Instant.now()

//    @Transient
     val identifier: Long
        get() = id!!

    @PrePersist
    fun prePersist() {
        createdAt = Instant.now()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        other as BaseEntity

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}