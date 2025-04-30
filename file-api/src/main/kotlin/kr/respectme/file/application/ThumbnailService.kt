package kr.respectme.file.application

import kr.respectme.file.application.dto.ImageFileCommandModelDto
import kr.respectme.file.application.dto.ImageFileCreateCommand
import kr.respectme.file.application.usecase.ThumbnailUseCase
import kr.respectme.file.common.utility.ThumbnailMaker
import kr.respectme.file.domain.ImageEntity
import kr.respectme.file.domain.ImageFileAccessPoint
import kr.respectme.file.domain.enums.FileFormat
import kr.respectme.file.domain.enums.ImageType
import kr.respectme.file.port.`in`.events.event.FileUploadedEvent
import kr.respectme.file.port.out.file.FileTransferService
import kr.respectme.file.port.out.file.FileUploadWrapper
import kr.respectme.file.port.out.persistent.SaveImagePort
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

@Service
class ThumbnailService(
    private val thumbnailMaker: ThumbnailMaker,
    private val fileTransferService: FileTransferService,
//    private val transferManager: TransferManager,
    private val saveImagePort: SaveImagePort,
    private val applicationEventPublisher: ApplicationEventPublisher,
    @Value("\${respect-me.cloud.aws.s3.bucket}")
    private val storageOrigin: String
): ThumbnailUseCase {

    private final val THUMBNAIL_ROOT_DIR_NAME = "thumbnails"

    @Transactional
    override fun createThumbnail(loginId: UUID, command: ImageFileCreateCommand)
    : ImageFileCommandModelDto {
        val originalImage = ImageIO.read(command.image.inputStream)
        val thumbnail = thumbnailMaker.makeThumbnail(originalImage)
        val outputStream = ByteArrayOutputStream()
        val fileSize = ImageIO.write(thumbnail, "jpg", outputStream)
        val size = outputStream.size().toLong()

        val imageFile = ImageEntity(
            memberId = loginId,
            imageFormat = FileFormat.JPEG,
            imageType = ImageType.THUMBNAIL,
            fileSize = size,
            width = thumbnail.width,
            height = thumbnail.height
        )

        val imageFileAccessPoint = ImageFileAccessPoint(uploadKey = UUID.randomUUID())
        imageFile.addAccessPoint(imageFileAccessPoint)
        saveImagePort.save(imageFile)

        val wrapper = FileUploadWrapper(
            rootDir = THUMBNAIL_ROOT_DIR_NAME,
            fileFormat = "jpg",
            accessKey = imageFileAccessPoint.uploadKey.toString(),
            file = outputStream.toByteArray()
        )
        val uploadResult = fileTransferService.upload(
            origin = storageOrigin,
            root = wrapper.rootDir,
            fileName = wrapper.accessKey,
            contentType = "image/jpeg",
            inputStream = wrapper.file.inputStream(),
            size = size,
        )
        publishFileUploadedEvent(uploadResult.toURL())

        val dto = ImageFileCommandModelDto.valueOf(command.image, imageFile, uploadResult)
        dto.accessUrl = fileTransferService.pathToCDNAccessURL(uploadResult.getPath())
        return dto
    }

    private fun publishFileUploadedEvent(fullPath: String) {
        val event = FileUploadedEvent(this, fullPath)
        applicationEventPublisher.publishEvent(event)
    }
}