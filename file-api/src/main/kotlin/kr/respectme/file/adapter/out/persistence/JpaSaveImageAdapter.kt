package kr.respectme.file.adapter.out.persistence

import kr.respectme.file.adapter.out.persistence.repository.ImageFileRepository
import kr.respectme.file.domain.ImageEntity
import kr.respectme.file.port.out.persistent.SaveImagePort
import org.springframework.stereotype.Repository

@Repository
class JpaSaveImageAdapter(private val imageFileRepository: ImageFileRepository):
SaveImagePort {

    override fun save(image: ImageEntity): ImageEntity {
        return imageFileRepository.save(image)
    }
}