package kr.respectme.common.domain.annotations

import kr.respectme.common.domain.BaseDomainEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Component

/**
 * DomainEntity 애노테이션이 붙은 클래스가 BaseDomainEntity를 상속받았는지 확인하는 BeanPostProcessor
 * @param packageName BaseDomainEntity를 상속받은 클래스들이 위치한 패키지
 * @see BaseDomainEntity
 */
@Component
class DomainEntityAnnotationProcessor: BeanFactoryPostProcessor{

    private val logger = LoggerFactory.getLogger(this::class.java)

    init {
        logger.debug("DomainEntityAnnotationProcessor initialized.")
    }

    override fun postProcessBeanFactory(beanFactory: ConfigurableListableBeanFactory) {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AnnotationTypeFilter(DomainEntity::class.java))
        val basePackage = getRootPackage()
        logger.debug("base package name: ${basePackage}")
        val candidateClasses = scanner.findCandidateComponents(basePackage)


        logger.debug("Target candidates size : ${candidateClasses.size}")

        for(clazz in candidateClasses) {
            logger.debug("")
            checkClassImplementationBaseDomainEntity(Class.forName(clazz.beanClassName))
        }
    }

    private fun checkClassImplementationBaseDomainEntity(clazz: Class<*>) {
        logger.debug("Checking class ${clazz.name}")
        if(clazz.isAssignableFrom(BaseDomainEntity::class.java)) {
            throw IllegalArgumentException("Class ${clazz.name} should extend BaseDomainEntity")
        }
    }

    fun getRootPackage(): String {
        val mainClassName = Thread.currentThread().stackTrace
            .firstOrNull { it.className.contains("Application") && !it.className.startsWith("org.springframework") }
            ?.className
            ?: throw IllegalStateException("Main application class not found. Ensure your main class is properly configured.")

        return mainClassName.substringBeforeLast(".")
    }
}