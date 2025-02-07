package kr.respectme.file.port.out.persistent

import kr.respectme.file.domain.ImageEntity

interface LoadImagePort {

    fun load(imageId: Long): ImageEntity
}