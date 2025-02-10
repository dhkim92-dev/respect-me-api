package kr.respectme.file.application.usecase

import kr.respectme.file.application.dto.ImageFileCommandModelDto
import kr.respectme.file.application.dto.ImageFileCreateCommand
import kr.respectme.file.application.dto.ImageQueryModelDto
import kr.respectme.file.application.dto.ImagesQuery
import java.util.UUID

interface ThumbnailUseCase {

    fun createThumbnail(loginId: UUID, command: ImageFileCreateCommand): ImageFileCommandModelDto

    fun getThumbnails(query: ImagesQuery): List<ImageQueryModelDto>
}