package kr.respectme.member.applications.adapter.command.strategy

import com.google.firebase.FirebaseApp
import kr.respectme.member.domain.model.DeviceTokenType
import org.springframework.stereotype.Component

@Component
class TokenValidationStrategyFactory(
    private val firebaseApp: FirebaseApp
) {

    fun build(type: DeviceTokenType): DeviceTokenValidationStrategy {
        return when(type) {
            DeviceTokenType.FCM -> FcmTokenValidationStrategy(firebaseApp)
            else -> throw IllegalArgumentException("Unsupported device token type")
        }
    }
}