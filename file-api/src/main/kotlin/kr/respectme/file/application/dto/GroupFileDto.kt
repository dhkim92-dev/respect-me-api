package kr.respectme.file.application.dto

import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.domain.enums.FileFormat
import java.util.UUID

data class GroupFileDto(
    val fileId: UUID,
    val groupId: UUID,
    val uploaderId: UUID,
    val name: String,
    val fileFormat: FileFormat,
    val size: Long,
    val createdAt: String,
    val path: String
) {

    companion object {
        fun of(entity: GroupSharedFile): GroupFileDto {
            return GroupFileDto(
                fileId = entity.identifier,
                groupId = entity.groupId,
                uploaderId = entity.uploaderId,
                name = entity.name,
                fileFormat = entity.format,
                size = entity.fileSize,
                createdAt = entity.createdAt.toString(),
                path = entity.path
            )
        }
    }
}