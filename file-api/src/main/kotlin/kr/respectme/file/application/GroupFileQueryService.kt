package kr.respectme.file.application

import kr.respectme.common.error.NotFoundException
import kr.respectme.file.application.dto.GroupFileQueryModelDto
import kr.respectme.file.application.usecase.GroupFileQueryUsecase
import kr.respectme.file.common.errors.FileErrorCode
import kr.respectme.file.port.out.persistent.LoadGroupFilePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class GroupFileQueryService(
    private val loadGroupFilePort: LoadGroupFilePort,
    @Value("\${respect-me.cloud.aws.cloud-front.url}")
    private val cdnUrl: String
): GroupFileQueryUsecase {

    override fun getGroupFile(loginId: UUID, fileId: UUID): GroupFileQueryModelDto {
        // TODO : file을 먼저 조회 한 이후 groupId 를 가져와서 GroupService 를 통해 퀀한 체크를 수행하여 접근 제어를 해야한다.
        val groupFile = loadGroupFilePort.findByFileId(fileId)
            ?: throw NotFoundException(FileErrorCode.GroupSharedFileNotExists)

        val signedUrl = getSignedUrl(groupFile.path)
        val dto = GroupFileQueryModelDto.of(groupFile)
        dto.url = signedUrl
        return dto
    }

    private fun getSignedUrl(path: String): String {
        // TODO : 추후 Signed URL로 접근하도록 실제 구현을 해야함
        return "$cdnUrl/$path"
    }
}