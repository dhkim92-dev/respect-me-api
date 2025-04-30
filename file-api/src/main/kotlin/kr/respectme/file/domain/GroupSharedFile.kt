package kr.respectme.file.domain

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.PrePersist
import kr.respectme.common.utility.UUIDV7Generator
import kr.respectme.file.domain.converter.FileFormatConverter
import kr.respectme.file.domain.enums.FileFormat
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
class GroupSharedFile(
    fileId: UUID? = null,
    @Column
    val groupId: UUID = UUID.randomUUID(),
    @Column
    val uploaderId: UUID = UUID.randomUUID(),
    @Column
    val name: String = "",
    @Convert(converter = FileFormatConverter::class)
    val format: FileFormat = FileFormat.JPEG,
    path: String = "",
    @Column
    val fileSize: Long = 0L,
    @CreatedDate
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    val createdAt: Instant = Instant.now(),
    updatedAt: Instant? = null,
    deleted: Boolean = false,
) {
    @Id
    var fileId: UUID? = fileId
        protected set

    @Column
    var path: String = path
        protected set

    @Column
    var deleted: Boolean = deleted
        protected set

    @LastModifiedDate
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    var updatedAt: Instant? = updatedAt
        protected set

    val identifier: UUID
        get() = fileId!!

    fun validate() {
        if (fileSize <= 0) {
            throw IllegalArgumentException("File size must be non-negative")
        }
    }

    fun isNew(): Boolean {
        return fileId == null
    }

    @PrePersist
    private fun prePersist() {
        if (fileId != null) return;
        fileId = UUIDV7Generator.generate()
    }

    fun markDeleted(value: Boolean) {
        deleted = value
    }

    fun updatePath(newPath: String) {
        path = newPath
    }
}