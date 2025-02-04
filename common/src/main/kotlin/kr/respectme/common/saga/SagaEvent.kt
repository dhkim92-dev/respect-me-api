package kr.respectme.common.saga

import java.time.Instant
import java.util.UUID

data class SagaEvent<T>(
    val transactionId: UUID,
    val timestamp: Long = Instant.now().toEpochMilli(),
    val requiredLocks: List<String> = emptyList<String>(),
    val data: T? = null
) {
}