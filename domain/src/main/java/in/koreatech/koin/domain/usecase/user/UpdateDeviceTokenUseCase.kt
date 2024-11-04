package `in`.koreatech.koin.domain.usecase.user

import `in`.koreatech.koin.domain.repository.firebase.messaging.FirebaseMessagingRepository
import javax.inject.Inject

class UpdateDeviceTokenUseCase @Inject constructor(
    private val firebaseMessagingRepository: FirebaseMessagingRepository,
) {
    suspend operator fun invoke() {
        val deviceToken = firebaseMessagingRepository.getFcmToken()
        firebaseMessagingRepository.updateNewFcmToken(deviceToken)
    }
}