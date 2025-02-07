package kr.respectme.file.domain

import jakarta.persistence.*
import java.util.UUID

@Entity
class ImageFileAccessPoint(
    id: Long? = null,
    @Column
    val uploadKey: UUID
): BaseEntity(id) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    lateinit var image: ImageEntity
        protected set;

    fun setImageFileEntity(imageFile: ImageEntity) {
        this.image = imageFile
    }
}