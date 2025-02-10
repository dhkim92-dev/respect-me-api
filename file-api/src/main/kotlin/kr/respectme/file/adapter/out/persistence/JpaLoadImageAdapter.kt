package kr.respectme.file.adapter.out.persistence

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import kr.respectme.file.domain.ImageEntity
import kr.respectme.file.domain.ImageQueryModel
import kr.respectme.file.domain.QImageEntity
import kr.respectme.file.domain.QImageFileAccessPoint
import kr.respectme.file.port.out.persistent.LoadImagePort
import org.springframework.stereotype.Repository

@Repository
class JpaLoadImageAdapter(
    private val qf: JPAQueryFactory
): LoadImagePort {

    override fun load(imageId: Long): ImageEntity? {
        val image = QImageEntity.imageEntity
        val accessPoint = QImageFileAccessPoint.imageFileAccessPoint

        return qf.selectFrom(image)
            .leftJoin(image.accessPoint, accessPoint)
            .fetchJoin()
            .where(image.id.eq(imageId))
            .fetchOne()
    }

    override fun findByImageIn(imageIds: List<Long>): List<ImageQueryModel> {
        val image = QImageEntity.imageEntity
        val accessPoint = QImageFileAccessPoint.imageFileAccessPoint

//        val subQuery = qf.selectFrom(accessPoint)
//            .where(accessPoint.image.id.eq(image.id))
//            .orderBy(accessPoint.id.desc())
//            .limit(1)

        return qf.select(
            Projections.constructor(
                ImageQueryModel::class.java,
                image.id,
                image.memberId,
                accessPoint.uploadKey
            ))
            .from(image)
            .leftJoin(image.accessPoint, accessPoint)
            .orderBy(accessPoint.id.desc())
            .where(image.id.`in`(imageIds))
            .fetch()
    }
}