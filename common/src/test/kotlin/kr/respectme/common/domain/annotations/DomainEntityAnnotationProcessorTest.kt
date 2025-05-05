package kr.respectme.common.domain.annotations

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.spring.SpringExtension
import io.mockk.mockk
import kr.respectme.common.support.DomainEntityConfig
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.test.context.SpringBootTest
//import org.springframework.boot.test.context.TestConfiguration

class DomainEntityAnnotationProcessorTest: AnnotationSpec() {

    override fun extensions() = listOf(SpringExtension)

    @Test
    fun `BaseDomainEntity를 상속받지 않으면 에러가 발생한다`() {
        val processor = DomainEntityAnnotationProcessor()//"kr.respectme.common.domain.entity.fail")
        val beanFactory = mockk<ConfigurableListableBeanFactory>()
        shouldThrow<IllegalArgumentException> {
            processor.postProcessBeanFactory(beanFactory)
        }
    }

    @Test
    fun `BaseDomainEntity를 상속받으면 성공한다`() {
        val processor = DomainEntityAnnotationProcessor()//"kr.respectme.common.domain.entity.success")
        val beanFactory = mockk<ConfigurableListableBeanFactory>()
        shouldNotThrowAny {
            processor.postProcessBeanFactory(beanFactory)
        }
    }
}