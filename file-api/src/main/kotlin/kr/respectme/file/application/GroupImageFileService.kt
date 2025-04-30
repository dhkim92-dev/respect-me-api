package kr.respectme.file.application

import kr.respectme.common.error.UnsupportedMediaTypeException
import kr.respectme.file.application.dto.GroupFileDto
import kr.respectme.file.application.dto.GroupFileUploadCommand
import kr.respectme.file.application.usecase.GroupFileUsecase
import kr.respectme.file.common.errors.FileErrorCode
import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.domain.enums.FileFormat
import kr.respectme.file.port.out.file.FileTransferService
import kr.respectme.file.port.out.persistent.LoadGroupFilePort
import kr.respectme.file.port.out.persistent.SaveSharedImagePort
import org.apache.tika.Tika
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class GroupImageFileService(
    private val saveSharedImagePort: SaveSharedImagePort,
    private val loadSharedImagePort: LoadGroupFilePort,
    private val transferService: FileTransferService,
    @Value("\${respect-me.cloud.aws.s3.bucket}")
    private val s3BucketName: String
): GroupFileUsecase {

    companion object {
        private val supported_types = listOf(
            "image/jpg",
            "image/jpeg",
            "image/png",
        )
    }

    @Transactional
    override fun upload(memberId: UUID,
                        command: GroupFileUploadCommand): GroupFileDto {

        // TODO : Require GroupService to check member permission.
        // 기본적으로 어드민 이상의 유저만 업로드 가능.
        val fileBytes = command.file.inputStream.readBytes()
        val imageFormat = validateFileType(fileBytes)

        val uploadResult = transferService.upload(inputStream = fileBytes.inputStream(),
            fileName = createFileName(imageFormat),
            origin = s3BucketName,
            root = "private/files",
            contentType = command.file.contentType!!,
            size = fileBytes.size.toLong()
        )

        var groupSharedImage = GroupSharedFile(
            groupId = command.groupId,
            uploaderId = memberId,
            name = command.file.originalFilename!!,
            format = mimeTypeToImageFormat(imageFormat),
            fileSize = fileBytes.size.toLong(),
            path = uploadResult.getPath()
        )
        groupSharedImage = saveSharedImagePort.persist(groupSharedImage)
        // TODO : Require publishing application event to delete object when this transaction failed.

        return GroupFileDto.of(groupSharedImage)
    }

    private fun createFileName(mimeType: String): String {
        val name = UUID.randomUUID().toString()
        return "$name.${mimeType.split("/").last()}"
    }

    private fun validateFileType(bytes: ByteArray): String {
        val tika = Tika()
        val mimeType = tika.detect(bytes)

        if ( mimeType !in supported_types ) {
            throw UnsupportedMediaTypeException(errorCode = FileErrorCode.NotSupportedImageType)
        }

        return mimeType
    }

    private fun mimeTypeToImageFormat(mimeType: String): FileFormat {
        return when (mimeType) {
            "image/jpg" -> FileFormat.JPEG
            "image/jpeg" -> FileFormat.JPEG
            "image/png" -> FileFormat.PNG
            else -> throw UnsupportedMediaTypeException(errorCode = FileErrorCode.NotSupportedImageType)
        }
    }

    override fun isSupportFormat(file: MultipartFile): Boolean {
        val mimeType = file.contentType ?: return false;
        return mimeType in supported_types
    }
}