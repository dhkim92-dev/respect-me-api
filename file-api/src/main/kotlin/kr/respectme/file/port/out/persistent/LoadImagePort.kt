package kr.respectme.file.port.out.persistent

import kr.respectme.file.domain.ImageEntity
import kr.respectme.file.domain.ImageQueryModel

interface LoadImagePort {

    fun load(imageId: Long): ImageEntity?

    fun findByImageIn(imageIds: List<Long>): List<ImageQueryModel>
}