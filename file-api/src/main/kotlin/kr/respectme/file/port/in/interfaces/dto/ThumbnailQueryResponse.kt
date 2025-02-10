package kr.respectme.file.port.`in`.interfaces.dto

import kr.respectme.file.application.dto.ImageQueryModelDto
import kr.respectme.file.domain.enum.ImageFormat
import kr.respectme.file.domain.enum.ImageType
import kr.respectme.file.port.`in`.interfaces.vo.FileOwner

class ThumbnailQueryResponse(
    val id: Long,
    val url: String?
) {

    companion object {
        fun valueOf(dto: ImageQueryModelDto): ThumbnailQueryResponse {
            return ThumbnailQueryResponse(
                id = dto.id,
                url = dto.url
            )
        }
    }
}