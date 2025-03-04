package kr.respectme.file.adapter.out.file

import com.oracle.bmc.objectstorage.ObjectStorage
import com.oracle.bmc.objectstorage.ObjectStorageClient
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest
import com.oracle.bmc.objectstorage.requests.PutObjectRequest
import kr.respectme.file.common.utility.CDNAccessPointBuilder
import kr.respectme.file.configs.CDNConfig
import kr.respectme.file.configs.OracleObjectStorageConfig
import kr.respectme.file.port.out.file.FileUploadResult
import kr.respectme.file.port.out.file.FileUploadWrapper
import kr.respectme.file.port.out.file.TransferManager
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test")
class OracleFileTransferManager(
    private val client: ObjectStorage,
    private val cdnConfig: CDNConfig,
    private val cdnAccessPointBuilder: CDNAccessPointBuilder
) : TransferManager {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun uploadFile(wrapper: FileUploadWrapper): FileUploadResult {
        val filePath = cdnAccessPointBuilder.buildStoragePath(wrapper.rootDir, wrapper.accessKey, wrapper.fileFormat)
        logger.debug("Oracle file path : ${filePath}")

        val request = PutObjectRequest.builder()
            .bucketName(cdnConfig.storageOrigin)
            .putObjectBody(wrapper.file.inputStream())
            .contentLength(wrapper.file.size.toLong())
            .objectName(filePath)
//            .contentType("image/${wrapper.fileFormat.lowercase()}")
            .namespaceName(cdnConfig.namespace)
            .build()

        try {
            client.putObject(request)
        } catch (e: Exception) {
            throw e
        }

        return FileUploadResult(
            storedPath = filePath,
            accessUrl =  cdnAccessPointBuilder.buildAccessUrl(wrapper.rootDir, wrapper.accessKey, wrapper.fileFormat)
        )
    }

    override fun deleteFile(path: String) {
        val request = DeleteObjectRequest.builder()
            .bucketName(cdnConfig.storageOrigin)
            .objectName(path)
            .build()
    }
}