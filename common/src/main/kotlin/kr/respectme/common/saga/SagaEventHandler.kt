package kr.respectme.common.saga

import org.springframework.transaction.annotation.Transactional
import org.springframework.util.backoff.BackOff
import kotlin.math.pow

abstract class SagaHandler {

    @Transactional
    fun <T> handleEvent(event: SagaEvent<T>,
                        maxRetry: Int = 1,
                        delay: Long = 100,
                        maxTimeout: Long = 5000,
                        multiplier: Double = 2.0
    ) {

        if(maxRetry < 1) {
            throw IllegalArgumentException("retryCount must be greater than 0")
        }

        var retryCount = 0

        while( retryCount < maxRetry ) {
            try {
                handle(event)
                return
            } catch (e: Exception) {
                retryCount++

                if(retryCount >= maxRetry) {
                    doOnFailed(event)
                    throw e
                }
                val nextDelay = (delay * multiplier.pow(retryCount)).toLong()

                Thread.sleep(if(nextDelay > maxTimeout) maxTimeout else nextDelay)
            }
        }
    }

    protected abstract fun <T> handle(event: SagaEvent<T>)

    protected abstract fun <T> doOnFailed(event: SagaEvent<T>)
}