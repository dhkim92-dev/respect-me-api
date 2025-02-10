package kr.respectme.file.application.dto

import kr.respectme.file.port.`in`.interfaces.dto.ThumbnailsQueryRequest

data class ImagesQuery(
    val imageIds: List<Long>
) {
    companion object {
        fun valueOf(request: ThumbnailsQueryRequest): ImagesQuery {
            return ImagesQuery(
                imageIds = request.imageIds
            )
        }
    }
}