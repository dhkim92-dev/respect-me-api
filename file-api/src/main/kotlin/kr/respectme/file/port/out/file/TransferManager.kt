package kr.respectme.file.port.out.file

import java.io.ByteArrayOutputStream
import java.util.UUID

interface TransferManager {

    fun uploadFile(wrapper: FileUploadWrapper): FileUploadResult

    fun deleteFile(path: String)

    fun getFileAccessUrl(rootDir: String, accessKey: UUID?): String?
}