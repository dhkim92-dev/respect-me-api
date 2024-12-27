package kr.respectme.common.kafka

import org.apache.kafka.clients.consumer.ConsumerInterceptor
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.slf4j.MDC


class TraceIdConsumerInterceptor: ConsumerInterceptor<Any, Any> {
    override fun configure(p0: MutableMap<String, *>?) {
    }

    override fun close() {
    }

    override fun onCommit(p0: MutableMap<TopicPartition, OffsetAndMetadata>?) {
    }

    override fun onConsume(p0: ConsumerRecords<Any, Any>?): ConsumerRecords<Any, Any> {
        p0?.forEach {
            val traceId = it.headers().lastHeader("traceId")?.value()?.toString(Charsets.UTF_8)
            MDC.put("traceId", traceId)
        }

        return p0!!
    }
}