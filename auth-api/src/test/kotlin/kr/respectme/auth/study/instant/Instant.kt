package kr.respectme.auth.study.instant

import org.junit.jupiter.api.Test
import java.time.Duration

class Instant {

    @Test
    fun test() {
        val now = java.time.Instant.now()
        println(now.toString())
        val nxt = now.plus(Duration.ofMillis(365L*24L*60L*60L*1000L))
        println(nxt.toString())
    }
}