package kr.respectme.member.applications.adapter.command.strategy

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

class FcmTokenValidationStrategy(private val app: FirebaseApp)
    : DeviceTokenValidationStrategy {

    override fun validate(token: String): Boolean {
        val message = Message.builder()
            .setToken(token)
            .build()

        return try {
            FirebaseMessaging.getInstance(app)
                .send(message)
            true
        } catch(e: FirebaseMessagingException) {
            false
        }
    }
}