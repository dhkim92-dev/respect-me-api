package kr.respectme.common.domain.configs

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import kr.respectme.common.domain.DomainEntityCacheAutoConfiguration
import kr.respectme.common.support.DomainEntityConfig
import kr.respectme.common.support.TestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [TestConfig::class, DomainEntityConfig::class])
class ConfigTest(@Autowired private val config: DomainEntityCacheAutoConfiguration): AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Test
    fun `config이 정상적으로 초기화 된다`() {
    }
}