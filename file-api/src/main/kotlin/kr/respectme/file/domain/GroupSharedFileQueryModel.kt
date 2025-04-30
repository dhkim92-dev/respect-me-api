package kr.respectme.file.domain

import kr.respectme.file.domain.enums.FileFormat
import java.time.Instant
import java.util.UUID

class GroupSharedFileQueryModel(
    val fileId: UUID,
    val groupId: UUID,
    val uploaderId: UUID,
    val name: String,
    val fileFormat: FileFormat,
    val size: Long,
    val createdAt: Instant,
    val path: String
) {

}