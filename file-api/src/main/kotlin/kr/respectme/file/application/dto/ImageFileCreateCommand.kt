package kr.respectme.file.application.dto

import org.springframework.web.multipart.MultipartFile

data class ImageFileCreateCommand(
    val image: MultipartFile
) {

    companion object {

        fun of(file: MultipartFile): ImageFileCreateCommand {
            return ImageFileCreateCommand(file)
        }
    }
}