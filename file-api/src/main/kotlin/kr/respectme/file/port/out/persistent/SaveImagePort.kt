package kr.respectme.file.port.out.persistent

import kr.respectme.file.domain.ImageEntity
import java.util.UUID

interface SaveImagePort {

    fun save(image: ImageEntity): ImageEntity
}