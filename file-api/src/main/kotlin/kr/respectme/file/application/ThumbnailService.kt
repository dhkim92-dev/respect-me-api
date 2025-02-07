package kr.respectme.file.application

import kr.respectme.file.application.dto.ImageFileCommandModelDto
import kr.respectme.file.application.dto.ImageFileCreateCommand
import kr.respectme.file.application.usecase.ThumbnailUseCase
import kr.respectme.file.common.utility.ThumbnailMaker
import kr.respectme.file.domain.ImageEntity
import kr.respectme.file.domain.ImageFileAccessPoint
import kr.respectme.file.domain.enum.ImageFormat
import kr.respectme.file.domain.enum.ImageType
import kr.respectme.file.port.`in`.events.event.FileUploadedEvent
import kr.respectme.file.port.out.file.FileUploadWrapper
import kr.respectme.file.port.out.file.TransferManager
import kr.respectme.file.port.out.persistent.SaveImagePort
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

@Service
class ThumbnailService(
    private val thumbnailMaker: ThumbnailMaker,
    private val transferManager: TransferManager,
    private val saveImagePort: SaveImagePort,
    private val applicationEventPublisher: ApplicationEventPublisher
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
            imageFormat = ImageFormat.JPEG,
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
        val uploadResult = transferManager.uploadFile(wrapper)
        publishFileUploadedEvent(uploadResult.storedPath)

        return ImageFileCommandModelDto.valueOf(command.image, imageFile, uploadResult)
    }

    private fun publishFileUploadedEvent(fullPath: String) {
        val event = FileUploadedEvent(this, fullPath)
        applicationEventPublisher.publishEvent(event)
    }
}