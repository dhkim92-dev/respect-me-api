package kr.respectme.file.unit.config

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import kr.respectme.file.configs.AWSConfig
import kr.respectme.file.support.AwsConfigGenerator

class AWSConfigTest : AnnotationSpec() {

    @Test
    fun `AWSConfig 생성 테스트`() {
        val accessKey = "testAccessKey"
        val secretKey = "testSecretKey"
        val region = "ap-northeast-2"

        shouldNotThrowAny {
            val awsConfig = AWSConfig(accessKey, secretKey, region)
        }
    }

    @Test
    fun `S3Client 생성 테스트`() {
        val config = AwsConfigGenerator.generateAwsConfig()

        val client = shouldNotThrowAny {
            config.s3Client()
        }
    }
}