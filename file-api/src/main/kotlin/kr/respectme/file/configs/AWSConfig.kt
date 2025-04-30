package kr.respectme.file.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class AWSConfig(
    @Value("\${respect-me.cloud.aws.s3.access-key}")
    private val accessKey: String,
    @Value("\${respect-me.cloud.aws.s3.secret-key}")
    private val secretKey: String,
    @Value("\${respect-me.cloud.aws.region}")
    private val region: String
) {

    @Bean
    fun awsBasicCredentials(): AwsBasicCredentials {
        return AwsBasicCredentials.create(accessKey, secretKey)
    }

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .credentialsProvider { awsBasicCredentials() }
            .region(Region.of(region))
            .build()
    }
}