package `in`.koreatech.koin.domain.repository.firebase.messaging

interface FirebaseMessagingRepository {
    suspend fun getFcmToken(): String
    suspend fun updateNewFcmToken(token: String)
}