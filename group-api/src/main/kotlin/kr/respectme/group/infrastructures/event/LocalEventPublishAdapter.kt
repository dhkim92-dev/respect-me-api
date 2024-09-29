package kr.respectme.group.infrastructures.event

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

//@Profile("local")
// TODO : @Profile("local") 어노테이션을 사용하여 로컬 환경에서만 사용하도록 설정
@Component
class LocalEventPublishAdapter(private val objectMapper: ObjectMapper) : EventPublishPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun publish(eventName: String, data: Any) {
        logger.info("event published. ${eventName} : ${objectMapper.writeValueAsString(data)}")
    }
}