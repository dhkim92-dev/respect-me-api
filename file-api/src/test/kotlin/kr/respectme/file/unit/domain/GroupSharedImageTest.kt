package kr.respectme.file.unit.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import kr.respectme.file.domain.GroupSharedFile
import kr.respectme.file.domain.enums.FileFormat
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.time.Instant
import java.util.UUID


@DataJpaTest
class GroupSharedImageTest(
    private val em: TestEntityManager
): AnnotationSpec() {

    override fun extensions(): List<Extension> {
        return listOf(SpringExtension)
    }

    @Test
    fun `엔티티 생성 테스트`() {
        val entity = createNewEntity()
        em.persist(entity)
        val id = shouldNotThrowAny {
            entity.identifier
        }

        id::class shouldBe UUID::class
        print(id)
    }

    @Test
    fun `isNew() 메서드 테스트`() {
        val entity = createNewEntity()

        entity.isNew() shouldBe true

        shouldNotThrowAny { em.persist(entity) }

        entity.isNew() shouldBe false
    }

    companion object {
        fun createNewEntity() =
            GroupSharedFile(
                fileId = null,
                uploaderId = UUID.randomUUID(),
                groupId = UUID.randomUUID(),
                name = "test.jpg",
                fileSize = 1024L,
                format = FileFormat.JPEG,
                path = "https://cdn.noti-me.com/test.jpg",
                createdAt = Instant.now(),
            )
    }
}