package kr.respectme.file.adapter.out.persistence

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.domain.GroupSharedFileQueryModel
import kr.respectme.file.port.out.persistent.LoadGroupFilePort
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JpaLoadGroupSharedImageAdapter(
    @PersistenceContext
    private val em: EntityManager,
    private val context: JpqlRenderContext
): LoadGroupFilePort {

    override fun loadById(fileId: UUID): GroupSharedFile? {
        return em.find(GroupSharedFile::class.java, fileId)
    }

//    data class Row(
//        val fileId: UUID,
//        val groupId: UUID,
//    )
//
//    fun findByGroupId(groupId: UUID): Row {
//        val context = JpqlRenderContext()
//        val query = jpql {
//            selectNew<Row>(
//                path(GroupSharedImage::fileId),
//                path(GroupSharedImage::groupId)
//            ).from(entity(GroupSharedImage::class)
//            ).where(
//                path(GroupSharedImage::groupId).eq(groupId)
//            )
//        }
//
//        return em.createQuery(query, context).singleResult
//    }

    override fun findByFileId(fileId: UUID): GroupSharedFileQueryModel? {
        val query = jpql {
            selectNew<GroupSharedFileQueryModel>(
                path(GroupSharedFile::fileId),
                path(GroupSharedFile::groupId),
                path(GroupSharedFile::uploaderId),
                path(GroupSharedFile::name),
                path(GroupSharedFile::format),
                path(GroupSharedFile::fileSize),
                path(GroupSharedFile::createdAt),
                path(GroupSharedFile::path)
            ).from(entity(GroupSharedFile::class)
            ).where(
                path(GroupSharedFile::fileId).eq(fileId)
            )
        }

        return em.createQuery(query, context).singleResult
    }

//    override fun findByGroupId(groupId: UUID): List<GroupSharedImageQueryModel> {
//        TODO("Not yet implemented")
//    }
}