package kr.respectme.file.application.dto

import kr.respectme.file.port.`in`.interfaces.dto.GroupFileUploadRequest
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

data class GroupFileUploadCommand(
    val groupId: UUID,
    val file: MultipartFile
) {

    companion object {
        fun of(request: GroupFileUploadRequest): GroupFileUploadCommand {
            return GroupFileUploadCommand(
                groupId = request.groupId,
                file = request.file
            )
        }
    }
}