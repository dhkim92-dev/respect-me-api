package kr.respectme.file.port.`in`.interfaces

import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailQueryResponse
import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailsQueryRequest
import java.util.UUID

interface ThumbnailQueryPort {

    fun getImages(serviceAccount: UUID, request: ThumbnailsQueryRequest): List<ThumbnailQueryResponse>
}