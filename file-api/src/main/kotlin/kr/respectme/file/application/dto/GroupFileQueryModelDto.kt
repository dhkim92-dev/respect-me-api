package kr.respectme.file.application.dto

import kr.respectme.file.domain.GroupSharedFileQueryModel
import kr.respectme.file.domain.enums.FileFormat
import java.time.Instant
import java.util.UUID

data class GroupFileQueryModelDto(
    val fileId: UUID,
    val groupId: UUID,
    val uploaderId: UUID,
    val fileName: String,
    val fileFormat: FileFormat,
    val createdAt: Instant,
    val fileSize: Long,
    var url: String = ""
) {

    companion object {
        fun of(model: GroupSharedFileQueryModel): GroupFileQueryModelDto {
            return GroupFileQueryModelDto(
                fileId = model.fileId,
                groupId = model.groupId,
                uploaderId = model.uploaderId,
                fileName = model.name,
                fileFormat = model.fileFormat,
                createdAt = model.createdAt,
                fileSize = model.size,
            )
        }
    }
}