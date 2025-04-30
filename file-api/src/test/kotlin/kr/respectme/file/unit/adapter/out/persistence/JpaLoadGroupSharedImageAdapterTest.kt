package kr.respectme.file.unit.adapter.out.persistence

import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.respectme.file.adapter.out.persistence.JpaLoadGroupSharedImageAdapter
import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.domain.GroupSharedFileQueryModel
import kr.respectme.file.unit.domain.GroupSharedImageTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class JpaLoadGroupSharedImageAdapterTest(
    @PersistenceContext
    private val em: EntityManager
): AnnotationSpec() {

    private lateinit var port: JpaLoadGroupSharedImageAdapter

    private lateinit var existsEntity: GroupSharedFile

    private val jpqlContext = JpqlRenderContext()

    override fun extensions(): List<Extension> {
        return listOf(SpringExtension)
    }

    @BeforeEach
    fun setUp() {
        port = JpaLoadGroupSharedImageAdapter(em, jpqlContext)
        existsEntity = GroupSharedImageTest.createNewEntity()
        em.persist(existsEntity)
        em.flush()
        em.detach(existsEntity)
    }

    @Test
    fun `영속화 된 엔티티를 조회하면 정상적으로 조회된다`() {
        // Given
        val entity = existsEntity

        // When
        val loadedEntity = port.loadById(entity.identifier)
        em.flush()

        // Then
        loadedEntity shouldNotBe null
        loadedEntity as GroupSharedFile
        loadedEntity.identifier shouldBe entity.identifier
        loadedEntity.groupId shouldBe entity.groupId
        loadedEntity.name shouldBe entity.name
        loadedEntity.uploaderId shouldBe entity.uploaderId
        loadedEntity.format shouldBe entity.format
        loadedEntity.deleted shouldBe entity.deleted
        loadedEntity.path shouldBe entity.path
        loadedEntity.fileSize shouldBe entity.fileSize
    }

    @Test
    fun `QueryProjection 테스트`() {
        // Given
        val id = existsEntity.identifier

        // When
        val qm = port.findByFileId(id)

        // Then
        qm shouldNotBe null
        qm as GroupSharedFileQueryModel
        qm.fileId shouldBe id
        qm.groupId shouldBe existsEntity.groupId
        qm.name shouldBe existsEntity.name
        qm.uploaderId shouldBe existsEntity.uploaderId
        qm.fileFormat shouldBe existsEntity.format
        qm.size shouldBe existsEntity.fileSize
        qm.createdAt shouldBe existsEntity.createdAt
        qm.path shouldBe existsEntity.path
    }

    @AfterEach
    fun cleanUp() {
        em.clear()
    }
}