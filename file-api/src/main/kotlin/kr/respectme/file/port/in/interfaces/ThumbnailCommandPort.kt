package kr.respectme.file.port.`in`.interfaces

import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailUploadRequest
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailUploadResponse
import java.util.UUID

interface ThumbnailCommandPort {

    fun createThumbnail(loginId: UUID, request: ThumbnailUploadRequest): ThumbnailUploadResponse
}