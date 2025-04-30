package kr.respectme.file.port.out.persistent

import kr.respectme.file.domain.GroupSharedFile
import java.util.UUID

interface SaveSharedImagePort {

    fun persist(image: GroupSharedFile): GroupSharedFile

    fun update(image: GroupSharedFile): GroupSharedFile

    fun delete(image: GroupSharedFile)

    fun deleteById(fileId: UUID)
}