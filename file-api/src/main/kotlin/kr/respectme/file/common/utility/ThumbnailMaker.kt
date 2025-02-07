package kr.respectme.file.common.utility

import net.coobird.thumbnailator.Thumbnails
import org.springframework.stereotype.Service
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

@Service
class ThumbnailMaker {

    fun makeThumbnail(image: BufferedImage): BufferedImage {
        val croppedImage = cropImage(image)
        val thumbnail = Thumbnails.of(croppedImage)
            .size(128, 128)
            .asBufferedImage()
        val jpegThumbnail = toJpeg(thumbnail)

        return jpegThumbnail
    }

    private fun cropImage(originalImage: BufferedImage): BufferedImage {
        val length = Math.min(originalImage.width, originalImage.height)
        val centerX = (originalImage.width) / 2
        val centerY = (originalImage.height) / 2
        val x = centerX - (length / 2)
        val y = centerY - (length / 2)

        return originalImage.getSubimage(x, y, length, length)
    }

    private fun toJpeg(image: BufferedImage): BufferedImage {
        val jpegImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)
        // 배경을 흰색으로 변경
        val graphics: Graphics2D = jpegImage.createGraphics()
        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, image.width, image.height) // 흰색 배경 채우기
        graphics.drawImage(image, 0, 0, null)
        graphics.dispose()
        return jpegImage
    }
}