package kr.respectme.common.domain

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.respectme.common.domain.cache.InMemoryDomainEntityCache
import kr.respectme.common.domain.entity.success.TestPassEntity
import kr.respectme.common.domain.entity.success.TestPassUUIDEntity
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.util.*

class InmemoryDomainEntityCacheTest(): AnnotationSpec() {

    private val objectMapper = Jackson2ObjectMapperBuilder.json()

    private val cacheStore = InMemoryDomainEntityCache(objectMapper as ObjectMapper)

    private lateinit var entity: TestPassEntity

    @BeforeEach
    fun setup() {
        cacheStore.evictAll()
        entity = TestPassEntity(1, "first commit")
        cacheStore.put(TestPassEntity::class.java, entity)
    }

    @Test
    fun `이미 추가된 Entity와 같은 ID 값을 가진 엔티티가 수정된 경우 isModified는 1을 반환한다`() {
        entity.name = "changed"
        cacheStore.isSameWithCache(TestPassEntity::class.java, entity) shouldBe 1
    }

    @Test
    fun `이미 추가된 Entity와 같은 ID값을 가진 엔티티가 수정되지 않은 경우 isModified는 0을 반환한다`() {
        cacheStore.isSameWithCache(TestPassEntity::class.java, entity) shouldBe 0
    }

    @Test
    fun `캐시에 저장되지 않은 Entity는 isModified는 -1을 반환한다`() {
        val newEntity = TestPassEntity(2, "new")
        cacheStore.isSameWithCache(TestPassEntity::class.java, newEntity) shouldBe -1
    }

    @Test
    fun `캐시에 저장되지 않은 Entity는 get은 null을 반환한다`() {
        cacheStore.get(TestPassEntity::class.java, 2) shouldBe null
    }

    @Test
    fun `캐시에 저장된 Entity를 조회하면 Entity가 반환된다`() {
        cacheStore.get(TestPassEntity::class.java, 1) shouldBe entity
    }

    @Test
    fun `캐시에 저장된 entity의 @DomainRelation 어노테이션이 붙은 필드를 제외한 다른 값에 변화가 없다면 isModified는 0을 반환한다`() {
        val newEntity = TestPassEntity(1, "first commit")
        newEntity.relations = listOf(TestPassUUIDEntity(UUID.randomUUID()))
        cacheStore.isSameWithCache(TestPassEntity::class.java, newEntity) shouldBe 0
    }

    @Test
    fun `캐시에 저장되지 않은 Entity를 조회하면 false가 반환된다`() {
        cacheStore.contains(TestPassEntity::class.java, 2) shouldBe false
    }

    @Test
    fun `캐시에 저장된 Entity를 조회하면 true가 반환된다`() {
        cacheStore.contains(TestPassEntity::class.java, 1) shouldBe true
    }

    @Test
    fun `evictAll을 호출하면 모든 엔티티가 삭제된다`() {
        cacheStore.evictAll()
        cacheStore.contains(TestPassEntity::class.java, 1) shouldBe false
    }
}