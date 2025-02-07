package kr.respectme.file.adapter.out.persistence.repository

import kr.respectme.file.domain.ImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageFileRepository: JpaRepository<ImageEntity, Long> {

}