package kr.respectme.file.port.out.persistent

import kr.respectme.file.domain.ImageEntity

interface SaveImagePort {

    fun save(image: ImageEntity): ImageEntity
}