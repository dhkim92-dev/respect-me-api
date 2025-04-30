package kr.respectme.file.port.out.persistent

import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.domain.GroupSharedFileQueryModel
import java.util.UUID

interface LoadGroupFilePort {

    fun loadById(fileId: UUID): GroupSharedFile?

    fun findByFileId(fileId: UUID): GroupSharedFileQueryModel?

//    fun findByGroupId(groupId: UUID): List<GroupSharedImageQueryModel>
}