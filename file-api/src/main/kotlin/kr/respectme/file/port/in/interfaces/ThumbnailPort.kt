package kr.respectme.file.port.`in`.interfaces

import jakarta.validation.Valid
import kr.respectme.file.common.annotation.ImageBytes
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailUploadRequest
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailUploadResponse
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface ThumbnailPort {

    fun createThumbnail(loginId: UUID, request: ThumbnailUploadRequest): ThumbnailUploadResponse
}