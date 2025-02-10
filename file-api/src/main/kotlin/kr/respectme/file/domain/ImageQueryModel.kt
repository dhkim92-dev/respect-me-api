package kr.respectme.file.domain

import java.util.UUID


data class ImageQueryModel(
    val id: Long,
    val ownerId: UUID,
    val accessKey: UUID
) {
}