package kr.respectme.file.support

import kr.respectme.file.configs.AWSConfig

class AwsConfigGenerator {

    companion object {
        fun generateAwsConfig(): AWSConfig{
            return AWSConfig(
                accessKey = System.getenv("RESPECT_ME_AWS_S3_ACCESS_KEY_ID"),
                secretKey = System.getenv("RESPECT_ME_AWS_S3_SECRET_ACCESS_KEY"),
                region = System.getenv("RESPECT_ME_AWS_REGION")
            )
        }
    }
}