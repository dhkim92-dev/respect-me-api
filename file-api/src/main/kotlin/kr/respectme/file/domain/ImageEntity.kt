package kr.respectme.file.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import kr.respectme.file.domain.enums.FileFormat
import kr.respectme.file.domain.enums.ImageType
import java.util.UUID

@Entity
@Table(name = "image_info")
class ImageEntity(
    id: Long? = null,
    @Column
    val memberId: UUID,
    @Column
    val imageFormat: FileFormat = FileFormat.JPEG,
    @Column
    val imageType: ImageType = ImageType.THUMBNAIL,
    @Column
    val fileSize: Long = 0L,
    @Column
    val width: Int = 128,
    @Column
    val height: Int = 128,
): BaseEntity(id) {

    @OneToMany(fetch = FetchType.LAZY,
        orphanRemoval = true,
        mappedBy = "image",
        cascade = [CascadeType.ALL])
    var accessPoint: MutableSet<ImageFileAccessPoint> = mutableSetOf()
        protected set;

    fun addAccessPoint(accessPoint: ImageFileAccessPoint) {
        this.accessPoint.add(accessPoint)
        accessPoint.setImageFileEntity(this)
    }
}