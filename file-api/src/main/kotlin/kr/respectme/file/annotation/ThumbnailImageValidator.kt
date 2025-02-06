package kr.respectme.file.annotation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO


class MultipartFileImageValidateProcessor: ConstraintValidator<MultipartFileImageValidate, MultipartFile> {

    private final val TARGET_WIDTH = 128
    private final val TARGET_HEIGHT = 128

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) {
            return false
        }
        val allowedContentTypes = listOf("image/jpeg", "image/png", "image/jpg")

        if(!allowedContentTypes.contains(value.contentType)){
            return false
        }

        val imageBytes = value.bytes
        val image = ImageIO.read(ByteArrayInputStream(imageBytes))
        val width = image.width
        val height = image.height

        return width == TARGET_WIDTH && height == TARGET_HEIGHT
    }
}