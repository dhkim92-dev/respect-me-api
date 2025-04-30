package kr.respectme.file.port.`in`.interfaces.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.respectme.file.application.dto.ImageFileCommandModelDto
import kr.respectme.file.domain.enums.FileFormat
import kr.respectme.file.domain.enums.ImageType
import kr.respectme.file.port.`in`.interfaces.vo.FileOwner

@Schema(description = "이미지 파일 업로드 응답 객체")
data class ThumbnailUploadResponse(
    val id: Long,
    val fileOwner: FileOwner,
    val originalFileName: String,
    val originalFileSize: Long,
    val imageType: ImageType = ImageType.THUMBNAIL,
    val imageFormat: FileFormat = FileFormat.JPEG,
    val thumbnailSize: Long,
    val url: String
) {

    companion object {

        fun valueOf(dto: ImageFileCommandModelDto): ThumbnailUploadResponse {
            return ThumbnailUploadResponse(
                id = dto.id,
                fileOwner = dto.fileOwner,
                originalFileName = dto.originalFileName,
                originalFileSize = dto.originalFileSize,
                thumbnailSize = dto.storedFileSize,
                imageType = dto.storedImageType,
                imageFormat = dto.storedImageFormat,
                url = dto.accessUrl,
            )
        }
    }
}