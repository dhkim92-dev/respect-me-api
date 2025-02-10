package kr.respectme.file.application.dto

import kr.respectme.file.domain.ImageQueryModel

data class ImageQueryModelDto(
    val id: Long,
    var url: String?=null
) {

    companion object {
        fun valueOf(model: ImageQueryModel, url: String?): ImageQueryModelDto {
            return ImageQueryModelDto(
                id = model.id,
                url = url
            )
        }
    }
}