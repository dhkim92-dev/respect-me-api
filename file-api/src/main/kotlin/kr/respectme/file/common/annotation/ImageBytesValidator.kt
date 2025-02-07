package kr.respectme.file.common.annotation

import jakarta.validation.ConstraintValidator
import org.springframework.web.multipart.MultipartFile
import javax.imageio.ImageIO

class ImageBytesValidator(): ConstraintValidator<ImageBytes, MultipartFile> {

    // 지원하는 이미지 매직 넘버 (파일 헤더)
    private val allowedSignatures = mapOf(
        "jpg" to listOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte()), // JPEG 헤더
        "png" to listOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte()) // PNG 헤더
    )

    override fun isValid(value: MultipartFile?, context: jakarta.validation.ConstraintValidatorContext?): Boolean {
        if (value == null || value.isEmpty) {
            return false
        }

        val fileBytes = value.bytes

        if(!isValidMagicNumber(fileBytes)) {
            return false
        }

        return try {
            val image = ImageIO.read(fileBytes.inputStream())
            image != null
        } catch(e: Exception) {
            false
        }
    }

    private fun isValidMagicNumber(fileBytes: ByteArray): Boolean {
        for((_, signature) in allowedSignatures) {
            if (fileBytes.size >= signature.size && fileBytes.take(signature.size) == signature) {
                return true
            }
        }
        return false
    }
}