package kr.respectme.file.port.`in`.interfaces.dto

import kr.respectme.file.application.dto.GroupFileQueryModelDto
import java.time.Instant
import java.util.UUID

data class GroupFileQueryResponse(
    val fileId: UUID,
    val groupId: UUID,
    val uploaderId: UUID,
    val fileName: String,
    val format: String,
    val accessUrl: String,
    val createdAt: Instant,
    val size: Long,
) {

    companion object {
        fun of(dto: GroupFileQueryModelDto): GroupFileQueryResponse {
            return GroupFileQueryResponse(
                fileId = dto.fileId,
                groupId = dto.groupId,
                uploaderId = dto.uploaderId,
                fileName = dto.fileName,
                format = dto.fileFormat.name,
                accessUrl = dto.url,
                createdAt = dto.createdAt,
                size = dto.fileSize
            )
        }
    }
}