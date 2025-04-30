package kr.respectme.file.adapter.out.persistence

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.respectme.common.error.NotFoundException
import kr.respectme.file.common.errors.FileErrorCode
import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.port.out.persistent.SaveSharedImagePort
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class JpaSaveGroupSharedImageAdapter (
    @PersistenceContext
    private val em: EntityManager
): SaveSharedImagePort {

    override fun persist(image: GroupSharedFile): GroupSharedFile {
        if (!image.isNew()) {
            throw IllegalStateException("Cannot update exists entity")
        }
        em.persist(image)
        return image
    }

    override fun update(image: GroupSharedFile): GroupSharedFile {
        if (image.isNew()) {
            throw IllegalStateException("Cannot update a new entity")
        }
        em.find(GroupSharedFile::class.java, image.identifier)
            ?: throw NotFoundException(FileErrorCode.GroupSharedFileNotExists)

        return em.merge(image)
    }

    override fun delete(image: GroupSharedFile) {
        em.remove(image)
    }

    override fun deleteById(fileId: UUID) {
        em.find(GroupSharedFile::class.java, fileId)?.let {
            em.remove(it)
        }
    }
}