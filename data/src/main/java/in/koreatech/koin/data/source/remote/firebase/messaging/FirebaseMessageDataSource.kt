package `in`.koreatech.koin.data.source.remote.firebase.messaging

interface FirebaseMessageDataSource {
    suspend fun getFcmToken(): String
    suspend fun updateNewFcmToken(token: String)
}