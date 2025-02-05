package kr.respectme.message.configs

import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.support.converter.StringJsonMessageConverter

@Configuration
class KafkaConfig(
    @Value("\${respect-me.kafka.bootstrap-servers}")
    private val bootstrapServer: String,
){

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs = mapOf(
            AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer
        )
        return KafkaAdmin(configs)
    }

    @Bean
    fun messageCreate(): NewTopic {
        return NewTopic("message-create-event", 1, 1)
    }

    @Bean
    fun messageSentEvent(): NewTopic {
        return NewTopic("message-sent-event", 1, 1)
    }
}