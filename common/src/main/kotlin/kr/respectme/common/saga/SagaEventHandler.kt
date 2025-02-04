package kr.respectme.common.saga

import org.springframework.transaction.annotation.Transactional
import kotlin.math.pow

abstract class SagaEventHandler<T: SagaEvent<*>> {

    @Transactional
    fun handleEvent(event: T,
                    maxRetry: Int = 1,
                    delay: Long = 100,
                    maxTimeout: Long = 5000,
                    multiplier: Double = 2.0
    ) {

        if(maxRetry < 1 || delay < 1 || multiplier < 1 || maxTimeout < 1) {
            throw IllegalArgumentException("maxRetry, delay, multiplier, maxTimeout must be greater than 0")
        }

        if(maxTimeout < delay) {
            throw IllegalArgumentException("maxTimeout must be greater than delay")
        }

        var retryCount = 0

        while( retryCount < maxRetry ) {
            try {
                handle(event)
                return
            } catch (e: Exception) {

                if(retryCount >= maxRetry-1) {
                    throw e
                }
                val nextDelay = (delay * multiplier.pow(retryCount)).toLong()
                Thread.sleep(if(nextDelay > maxTimeout) maxTimeout else nextDelay)
            }
            retryCount++
        }
    }

    abstract fun compensate(event: T)

    protected abstract fun handle(event: T)
}