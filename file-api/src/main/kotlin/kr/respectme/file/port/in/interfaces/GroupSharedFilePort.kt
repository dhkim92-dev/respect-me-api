package kr.respectme.file.port.`in`.interfaces

import kr.respectme.file.port.`in`.interfaces.dto.GroupFileQueryResponse
import kr.respectme.file.port.`in`.interfaces.dto.GroupFileUploadRequest
import kr.respectme.file.port.`in`.interfaces.dto.GroupFileUploadResponse
import java.util.UUID

interface GroupSharedFilePort {

    fun uploadImage(
        memberId: UUID,
        request: GroupFileUploadRequest
    ): GroupFileUploadResponse

    fun getGroupFile(
        loginId: UUID,
        fileId: UUID
    ): GroupFileQueryResponse
}