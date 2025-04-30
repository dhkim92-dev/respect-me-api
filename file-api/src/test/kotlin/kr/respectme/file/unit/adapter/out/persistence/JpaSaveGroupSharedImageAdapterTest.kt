package kr.respectme.file.unit.adapter.out.persistence

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import kr.respectme.file.adapter.out.persistence.JpaSaveGroupSharedImageAdapter
import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.port.out.persistent.SaveSharedImagePort
import kr.respectme.file.unit.domain.GroupSharedImageTest
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class JpaSaveGroupSharedImageAdapterTest(
    @PersistenceContext
    private val em: EntityManager
): AnnotationSpec() {

    private lateinit var repository: SaveSharedImagePort

    private lateinit var existsEntity: GroupSharedFile

    override fun extensions(): List<Extension> {
        return listOf(SpringExtension)
    }

    @BeforeEach
    fun setUp() {
//        em.entityManager.transaction.begin()
        repository = JpaSaveGroupSharedImageAdapter(em)
        val entity = GroupSharedImageTest.createNewEntity()
        existsEntity = repository.persist(entity)
        em.flush()
    }

    @Test
    fun `신규 생성한 엔티티를 저장하면 저장된다`() {
        // Given
        val newEntity = GroupSharedImageTest.createNewEntity()

        // When
        val savedEntity = repository.persist(newEntity)

        // Then
        savedEntity.isNew() shouldBe false
        shouldNotThrowAny {
            savedEntity.identifier
        }
    }

    @Test
    fun `이미 영속화 된 엔티티를 persist 하면 에러가 발생한다`() {
        // Given
        val entity = existsEntity

        // When
        val exception = shouldThrow<IllegalStateException> {
            repository.persist(entity)
        }

        // Then
        exception.message shouldBe "Cannot update exists entity"
    }

    @Test
    fun `신규 엔티티를 update 하려고 하면 에러가 발생한다` () {
        // Given
        val entity = GroupSharedImageTest.createNewEntity()

        // When
        val exception = shouldThrow<IllegalStateException> {
            repository.update(entity)
            em.flush()
        }

        // Then
        exception.message shouldBe "Cannot update a new entity"
    }

    @Test
    fun `기존 엔티티를 update 하면 정상적으로 update 된다`() {
        // Given
        val entity = existsEntity

        entity.markDeleted(true)

        // When
        val updatedEntity = repository.update(entity)
        em.flush()

        // Then
//        updatedEntity.identifier shouldBe entity.identifier
        println(updatedEntity.identifier)
        updatedEntity.groupId shouldBe entity.groupId
        updatedEntity.uploaderId shouldBe entity.uploaderId
        updatedEntity.name shouldBe entity.name
        updatedEntity.format shouldBe entity.format
        updatedEntity.path shouldBe entity.path
        updatedEntity.deleted shouldBe true
        updatedEntity.updatedAt shouldNotBe null
        println(updatedEntity.updatedAt)
//        updatedEntity.updatedAt shouldNotBe null
    }

    @Test
    fun `엔티티를 삭제하면 삭제된다`() {
        // Given
        val entity = existsEntity

        // When
        repository.delete(entity)
        em.flush()

        // Then
        val deletedEntity = em.find(GroupSharedFile::class.java, entity.identifier)
        deletedEntity shouldBe null
    }

    @AfterEach
    fun cleanUp() {
        em.clear()
    }
}