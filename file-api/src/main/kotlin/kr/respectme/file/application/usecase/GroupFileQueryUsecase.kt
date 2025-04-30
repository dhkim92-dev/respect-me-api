package kr.respectme.file.application.usecase

import kr.respectme.file.application.dto.GroupFileQueryModelDto
import java.util.UUID


interface GroupFileQueryUsecase {

    fun getGroupFile(loginId: UUID, fileId: UUID): GroupFileQueryModelDto
}