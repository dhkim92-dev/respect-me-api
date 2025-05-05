package kr.respectme.file.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.file.application.dto.GroupFileQueryModelDto
import java.time.Instant
import java.util.UUID

@Schema(description = "그룹 파일 조회 응답")
data class GroupFileQueryResponse(
    @Schema(description = "업로드 된 파일 식별자", example = "d67f4dfe-0d0f-406c-be67-71205404a013")
    val fileId: UUID,
    @Schema(description = "업로드 된 파일의 소유 그룹 ID", example = "a6bfcdfe-0d0f-406c-be67-abcde404a714")
    val groupId: UUID,
    @Schema(description = "업로드 된 파일의 소유자 ID", example = "b7bfcdfe-0d0f-406c-be67-abcde404a714")
    val uploaderId: UUID,
    @Schema(description = "업로드 된 파일의 원본 이름", example = "image.png")
    val fileName: String,
    @Schema(description = "업로드 된 파일의 포맷", example = "JPEG")
    val format: String,
    @Schema(description = "업로드 된 파일의 접근 경로", example = "https://cdn.noti-me.net/private/d/6/d67f4dfe-0d0f-406c-be67-71205404a013.png")
    val accessUrl: String,
    @Schema(description = "업로드 된 파일의 생성 시간", example = "2023-10-01T12:00:00Z")
    val createdAt: Instant,
    @Schema(description = "업로드 된 파일의 크기 (Bytes)", example = "1024")
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