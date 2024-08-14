package kr.respectme.member.common.persistent

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.EntityListeners
import jakarta.persistence.PreUpdate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class CreatedAtUpdatedAtFieldEntity {

    @CreatedDate
    @Column
    var createdAt: LocalDateTime = LocalDateTime.now()

    @LastModifiedDate
    @Column
    var updatedAt: LocalDateTime? = null

    @PreUpdate
    fun updateLastModifiedDate() {
        updatedAt = LocalDateTime.now()
    }
}