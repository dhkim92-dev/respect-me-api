package kr.respectme.file.unit.adapter.out.file

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.file.adapter.out.file.S3FileTransferService
import kr.respectme.file.support.AwsConfigGenerator

class S3FileTransferServiceTest: AnnotationSpec() {
    private val cdn = "d380gc0prbxdbr.cloudfront.net"
    private val awsConfig = AwsConfigGenerator.generateAwsConfig()
    private val service = S3FileTransferService(awsConfig.s3Client(), cdn)

    @Test
    fun `File Upload Test`() {
        val file = java.io.File("src/test/resources/test.txt")
        val result = shouldNotThrowAny {
            try {
                service.upload(
                    inputStream = file.inputStream(),
                    contentType = "text/plain",
                    origin = "noti-me",
                    root = "test",
                    fileName = "test.txt",
                    size = file.length()
                )
            } catch(e: Exception) {
                println("Error: ${e.message}")
                throw e
            }
        }

        result.getPath() shouldBe "test/t/e/test.txt"
        result.getOrigin() shouldBe "noti-me"
        result.getRoot() shouldBe "test"
        result.toURL() shouldBe "https://noti-me.s3.ap-northeast-2.amazonaws.com/test/t/e/test.txt"
    }

    @Test
    fun `CDN 경로 변환 테스트`() {
        val fullPath = "test/t/e/test.txt"
        val result = service.pathToCDNAccessURL(fullPath)
        result shouldBe "https://$cdn/$fullPath"
    }
}