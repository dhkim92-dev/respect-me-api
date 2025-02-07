package kr.respectme.file.port.out.file

import java.io.ByteArrayOutputStream

data class FileUploadWrapper(
    val rootDir: String,
    val fileFormat: String,
    val accessKey: String,
    val file: ByteArray
){

}