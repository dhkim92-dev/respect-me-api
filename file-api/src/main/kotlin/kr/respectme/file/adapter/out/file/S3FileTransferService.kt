package kr.respectme.file.adapter.out.file

import kr.respectme.file.adapter.out.file.dto.S3FileTransferResult
import kr.respectme.file.port.out.file.FileTransferResult
import kr.respectme.file.port.out.file.FileTransferService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectResponse
import java.io.InputStream
import java.net.URL

@Service
class S3FileTransferService(
    private val s3Client: S3Client,
    @Value("\${respect-me.cloud.aws.cloud-front.url}")
    private val cdnUrl: String,
): FileTransferService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun upload(inputStream: InputStream,
                        contentType: String,
                        origin: String,
                        root: String,
                        fileName: String,
                        size: Long): FileTransferResult {
        val key = createKey(root, fileName)
        val putObject = PutObjectRequest.builder()
            .bucket(origin)
            .key(key)
            .contentLength(size)
            .contentType(contentType)
            .build()

        try {
            request(putObject, inputStream, size)
        } catch (e: Exception) {
            logger.error("File upload failed, bucket: $origin, key: $key, error message: ${e.message}")
            throw RuntimeException("File upload failed: ${e.message}", e)
        }

        return S3FileTransferResult.of(
            region = s3Client.serviceClientConfiguration().region().toString(),
            bucket = origin,
            root = root,
            path = key
        )
    }

    override fun delete(url: String): Boolean {
        val parsedUrl = URL(url)
        val bucket = parsedUrl.host.split(".").last()
        val key = parsedUrl.path.substring(1)

        val deleteRequest = DeleteObjectRequest.builder()
            .bucket(bucket)
            .key(key)
            .build()

        try {
            s3Client.deleteObject(deleteRequest)
        } catch (e: Exception) {
            logger.error("File deletion failed, bucket: $bucket, key: $key, error message: ${e.message}")
            throw RuntimeException("File deletion failed: ${e.message}", e)
        }

        return true
    }

    override fun pathToCDNAccessURL(fullPath: String): String {
        return "$cdnUrl/$fullPath"
    }

    private fun createKey(root: String, fileName: String): String {
        return "$root/${fileName[0]}/${fileName[1]}/${fileName}"
    }

    private fun request(request: PutObjectRequest, inputStream: InputStream, size: Long)
        : PutObjectResponse {
        return s3Client.putObject(request, RequestBody.fromInputStream(inputStream, size))
    }
}