package kr.respectme.file.port.out.file

import java.io.ByteArrayOutputStream

interface TransferManager {

    fun uploadFile(wrapper: FileUploadWrapper): FileUploadResult

    fun deleteFile(path: String)
}