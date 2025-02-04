package kr.respectme.common.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.converter.StringJsonMessageConverter

@AutoConfiguration
@Configuration
@ConditionalOnProperty(prefix = "respect-me.kafka", name = ["enabled"], havingValue = "true", matchIfMissing = false)
open class DefaultKafkaConfig(
    @Value("\${respect-me.kafka.bootstrap-servers}")
    private val bootstrapServer: String,
    @Value("\${respect-me.kafka.consumer.group-id}")
    private val groupId: String,
    @Value("\${respect-me.kafka.auto-commit-reset:earliest}")
    private val autoCommitReset: String,
    @Value("\${respect-me.kafka.enable-auto-commit:false}")
    private val enableAutoCommit: Boolean,
    @Value("\${respect-me.kafka.producer.id}")
    private val producerId: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        logger.debug("kafka bootstrapServer: $bootstrapServer")
        val configProps: Map<String, Any> = mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.PARTITIONER_CLASS_CONFIG to "org.apache.kafka.clients.producer.internals.DefaultPartitioner", // 기본 파티셔너 사용
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true,
            ProducerConfig.INTERCEPTOR_CLASSES_CONFIG to listOf(TraceIdProducerInterceptor::class.java),
        )
        val producer = DefaultKafkaProducerFactory<String, String>(configProps)
        producer.setTransactionIdPrefix("${producerId}-tx") // 카프카 트랜잭션 이용
        return producer
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        logger.debug("Kafka Template created")
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun consumerFactory(): ConsumerFactory<String, String> {
        logger.debug("Kafka Consumer Factory created")
        val configProps: Map<String, Any> = mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServer,
            ConsumerConfig.GROUP_ID_CONFIG to groupId,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to autoCommitReset,
            ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to enableAutoCommit,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.INTERCEPTOR_CLASSES_CONFIG to listOf(TraceIdConsumerInterceptor::class.java),
            ConsumerConfig.ISOLATION_LEVEL_CONFIG to "read_committed"
        )

        // consumer kafka transaction
        return DefaultKafkaConsumerFactory(configProps)
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        logger.debug("Kafka Listener Container Factory created")
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
        factory.consumerFactory = consumerFactory()
        factory.setRecordMessageConverter(StringJsonMessageConverter())
        return factory
    }

    @Bean
    fun traceIdProducerInterceptor(): TraceIdProducerInterceptor {
        logger.debug("TraceIdProducerInterceptor created")
        return TraceIdProducerInterceptor()
    }

    @Bean
    fun traceIdConsumerInterceptor(): TraceIdConsumerInterceptor {
        logger.debug("TraceIdConsumerInterceptor created")
        return TraceIdConsumerInterceptor()
    }
}