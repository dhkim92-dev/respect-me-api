package kr.respectme.common.kafka

import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.slf4j.LoggerFactory
import org.slf4j.MDC

class TraceIdProducerInterceptor() : ProducerInterceptor<Any, Any> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onAcknowledgement(metadata: RecordMetadata?, exception: Exception?) {
    }

    override fun close() {
    }

    override fun configure(configs: MutableMap<String, *>?) {
    }

    override fun onSend(record: ProducerRecord<Any, Any>?): ProducerRecord<Any, Any> {
        logger.debug("TraceId added for kafka message")
        val traceId = MDC.get("traceId").toString() ?: null
        record?.headers()?.add("traceId", traceId?.toByteArray())
        return record!!
    }
}