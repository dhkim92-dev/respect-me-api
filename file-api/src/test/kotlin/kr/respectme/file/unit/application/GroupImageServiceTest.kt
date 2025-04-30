package kr.respectme.file.unit.application

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.respectme.file.adapter.out.file.dto.S3FileTransferResult
import kr.respectme.file.application.GroupImageFileService
import kr.respectme.file.application.dto.GroupFileUploadCommand
import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.domain.enums.FileFormat
import kr.respectme.file.port.out.file.FileTransferService
import kr.respectme.file.port.out.persistent.LoadGroupFilePort
import kr.respectme.file.port.out.persistent.SaveSharedImagePort
import org.springframework.mock.web.MockMultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.imageio.ImageIO

class GroupImageServiceTest : BehaviorSpec() {

    // Mocking the GroupImageService dependencies for testing

    private val saveSharedImagePort = mockk<SaveSharedImagePort>()
    private val fileTransferService = mockk<FileTransferService>()
    private val loadSharedImagePort = mockk<LoadGroupFilePort>()
    private val s3BucketName = "test-bucket"
    private val groupImageService = GroupImageFileService(
        saveSharedImagePort,
        loadSharedImagePort,
        fileTransferService,
        s3BucketName
    )

    private val imageId = UUID.randomUUID()
    private val groupId = UUID.randomUUID()
    private val testImage = createTestPng()
    private val uploaderId = UUID.randomUUID()
    private val idString = imageId.toString()

    private fun createTestPng(): ByteArray {
        val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB)
        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(image, "png", byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    init {
        val imageEntity: GroupSharedFile = GroupSharedFile(
            fileId = imageId,
            groupId = UUID.randomUUID(),
            uploaderId = UUID.randomUUID(),
            name = "test-image.png",
            format = FileFormat.PNG,
            fileSize = 1024L,
            path = "private/images/${idString[0]}/${idString[1]}/$idString.png",
        )

        beforeEach() {
        }

        given("이미지 업로드 커맨드가 주어진다.") {
            // Image Upload Command
            val command = GroupFileUploadCommand(
                groupId = groupId,
                file = MockMultipartFile(
                    "file",
                    "test-image.png",
                    "image/png",
                    testImage
                )
            )

            `when`("S3에 업로드에 성공하고, 메타 데이터를 영속화 하면") {
                every {
                    fileTransferService.upload(any(),any(), any(), any(), any(), any())
                } returns S3FileTransferResult(
                    region = "ap-northeast-2",
                    bucket = s3BucketName,
                    root = "private/images",
                    path = imageEntity.path
                )

                every {
                    saveSharedImagePort.persist(any())
                } returns imageEntity

                Then("GroupSharedImageDto가 반환된다") {
                    val result = groupImageService.upload(
                        memberId = uploaderId,
                        command = command
                    )

                    result.fileId shouldBe imageEntity.fileId
                    result.groupId shouldBe imageEntity.groupId
                    result.uploaderId shouldBe imageEntity.uploaderId
                    result.name shouldBe imageEntity.name
                    result.fileFormat shouldBe imageEntity.format
                    result.size shouldBe imageEntity.fileSize
                    result.createdAt shouldBe imageEntity.createdAt.toString()
                    result.path shouldBe imageEntity.path
                }
            }
        }
    }

    override fun afterEach(testCase: TestCase, result: TestResult) {
    }
}