package kr.respectme.file.application.usecase

import kr.respectme.file.application.dto.GroupFileDto
import kr.respectme.file.application.dto.GroupFileUploadCommand
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface GroupFileUsecase {

    fun upload(memberId: UUID, command: GroupFileUploadCommand): GroupFileDto

    fun isSupportFormat(file: MultipartFile): Boolean
}