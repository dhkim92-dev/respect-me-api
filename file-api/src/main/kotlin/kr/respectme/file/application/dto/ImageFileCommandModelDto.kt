package kr.respectme.file.application.dto

import kr.respectme.file.domain.ImageEntity
import kr.respectme.file.domain.enums.FileFormat
import kr.respectme.file.domain.enums.ImageType
import kr.respectme.file.port.`in`.interfaces.vo.FileOwner
import kr.respectme.file.port.out.file.FileTransferResult
import org.springframework.web.multipart.MultipartFile

data class ImageFileCommandModelDto(
    val id: Long,
    val fileOwner: FileOwner,
    val originalFileName: String,
    val originalFileSize: Long,
    val storedFileSize: Long,
    val storedImageType: ImageType,
    val storedImageFormat: FileFormat,
    val storedPath: String,
    var accessUrl: String
) {

    companion object {

        fun valueOf(file: MultipartFile,
                    entity: ImageEntity,
                    result: FileTransferResult
        ): ImageFileCommandModelDto {
            return ImageFileCommandModelDto(
                id = entity.identifier,
                fileOwner = FileOwner(entity.memberId),
                originalFileName = file.originalFilename?:file.name,
                originalFileSize = file.size,
                storedImageFormat = entity.imageFormat,
                storedImageType = entity.imageType,
                storedFileSize = entity.fileSize,
                storedPath = result.getPath(),
                accessUrl = result.toURL(),
            )
        }
    }
}