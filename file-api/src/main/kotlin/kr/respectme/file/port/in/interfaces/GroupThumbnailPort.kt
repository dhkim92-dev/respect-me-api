package kr.respectme.file.port.`in`.interfaces

import kr.respectme.file.port.`in`.interfaces.dto.GroupThumbnailCreateRequest
import kr.respectme.file.port.`in`.interfaces.dto.GroupThumbnailCreateResponse
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

interface GroupThumbnailPort {

    fun uploadGroupThumbnail(loginId: UUID, groupId: UUID, file: MultipartFile): GroupThumbnailCreateResponse

    fun deleteGroupThumbnail(loginId: UUID, groupId: UUID)
}