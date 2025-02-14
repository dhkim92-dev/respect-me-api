package kr.respectme.member.configs

import kr.respectme.member.common.saga.SagaEventDefinition
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaAdmin

@Configuration
class KafkaConfig(
    @Value("\${respect-me.kafka.bootstrap-servers}")
    private val bootStrapServers: String,
){


    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs = mutableMapOf<String, Any>()
        configs["bootstrap.servers"] = bootStrapServers
        return KafkaAdmin(configs)
    }

    @Bean
    fun memberDeletedTopic(): NewTopic {
        val topicConfiguration = mutableMapOf<String, String>()
        topicConfiguration["cleanup.policy"] = "delete"
        topicConfiguration["retention.ms"] = "604800000" // 이건 7일 회원 탈퇴 이벤트는 7일간 보관된다.
        return NewTopic(SagaEventDefinition.MEMBER_DELETE_SAGA, 1, 1).configs(topicConfiguration)
    }

    @Bean
    fun memberDeleteCompletedTopic(): NewTopic {
        val topicConfiguration = mutableMapOf<String, String>()
        topicConfiguration["cleanup.policy"] = "delete"
        topicConfiguration["retention.ms"] = "604800000" // 이건 7일 회원 탈퇴 이벤트는 7일간 보관된다.
        return NewTopic(SagaEventDefinition.MEMBER_DELETE_COMPLETED_SAGA, 1, 1).configs(topicConfiguration)
    }

    @Bean
    fun memberDeleteFailedTopic(): NewTopic {
        val topicConfiguration = mutableMapOf<String, String>()
        topicConfiguration["cleanup.policy"] = "delete"
        topicConfiguration["retention.ms"] = "604800000" // 이건 7일 회원 탈퇴 이벤트는 7일간 보관된다.
        return NewTopic(SagaEventDefinition.MEMBER_DELETE_FAILED_SAGA, 1, 1).configs(topicConfiguration)
    }
}