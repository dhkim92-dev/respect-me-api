package kr.respectme.file.common.errors

import kr.respectme.common.error.ErrorCode
import org.springframework.http.HttpStatus

enum class FileErrorCode(
    override val status: HttpStatus,
    override val code: String,
    override val message: String
): ErrorCode {

    FileFormatNotSupported(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "FILE-001", "File format not supported"),
    GroupSharedFileNotExists(HttpStatus.NOT_FOUND, "FILE-002", "Group shared file not exists"),
    NotSupportedImageType(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "FILE-003", "Not supported image type"),
}