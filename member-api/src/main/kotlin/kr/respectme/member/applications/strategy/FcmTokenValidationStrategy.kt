package kr.respectme.member.applications.strategy

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message

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