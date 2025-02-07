package kr.respectme.file.application.usecase

import kr.respectme.file.application.dto.ImageFileCommandModelDto
import kr.respectme.file.application.dto.ImageFileCreateCommand
import java.util.UUID

interface ThumbnailUseCase {

    fun createThumbnail(loginId: UUID, command: ImageFileCreateCommand): ImageFileCommandModelDto
}