package kr.respectme.file.adapter.out.file

import kr.respectme.common.error.InternalServerError
import kr.respectme.file.port.out.file.FileUploadResult
import kr.respectme.file.port.out.file.FileUploadWrapper
import kr.respectme.file.port.out.file.TransferManager
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.File

//@Service
//@Profile("local", "test")
//class LocalFileTransferManager(
//    private val cdnAccessPointBuilder: CDNAccessPointBuilder
//)
//: TransferManager {
//
//    private val logger = LoggerFactory.getLogger(javaClass)
//
//    override fun uploadFile(wrapper: FileUploadWrapper): FileUploadResult {
//        val fullPath = cdnAccessPointBuilder.buildStoragePath(wrapper.rootDir, wrapper.accessKey, wrapper.fileFormat)
//
//        try {
//            val file = File(fullPath)
//            file.parentFile?.mkdirs()
//            file.writeBytes(wrapper.file)
//            return FileUploadResult(
//                storedPath = fullPath,
//                accessUrl = cdnAccessPointBuilder.buildAccessUrl(wrapper.rootDir, wrapper.accessKey, wrapper.fileFormat)
//            )
//        } catch(e: Exception) {
//            logger.error("파일 업로드 실패, full path : ${fullPath} error message : ${e.message}")
//            throw InternalServerError("파일 업로드에 실패했습니다.")
//        }
//    }
//
//    override fun deleteFile(fullPath: String) {
//        val storedPath = cdnAccessPointBuilder.extractStoredPath(fullPath)
//        try {
//            val file = File(storedPath)
//            file.delete()
//        } catch(e: Exception) {
//            logger.error("파일 삭제 실패, full path : ${fullPath} error message : ${e.message}")
//            throw e
//        }
//    }
//}