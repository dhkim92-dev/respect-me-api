package port.`in`.interfaces

import port.`in`.interfaces.dto.GroupThumbnailCreateRequest
import port.`in`.interfaces.dto.GroupThumbnailCreateResponse
import java.util.UUID

interface GroupThumbnailPort {

    fun uploadGroupThumbnail(loginId: UUID, request: GroupThumbnailCreateRequest): GroupThumbnailCreateResponse
}