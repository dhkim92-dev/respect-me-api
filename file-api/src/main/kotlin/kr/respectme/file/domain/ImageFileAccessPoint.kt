package kr.respectme.file.domain

import jakarta.persistence.*
import java.util.UUID

@Entity
class ImageFileAccessPoint(
    id: Long? = null,
    @Column
    val uploadKey: UUID,
    image: ImageEntity? = null,
): BaseEntity(id) {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    var image: ImageEntity? = image
        protected set;

    fun setImageFileEntity(imageFile: ImageEntity?) {
        this.image = imageFile
    }
}