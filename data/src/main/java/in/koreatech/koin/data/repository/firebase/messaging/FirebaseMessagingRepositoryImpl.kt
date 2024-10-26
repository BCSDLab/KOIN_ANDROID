package `in`.koreatech.koin.data.repository.firebase.messaging

import `in`.koreatech.koin.data.source.remote.firebase.messaging.FirebaseMessageDataSource
import `in`.koreatech.koin.domain.repository.firebase.messaging.FirebaseMessagingRepository
import javax.inject.Inject

class FirebaseMessagingRepositoryImpl @Inject constructor(
    private val firebaseMessageDataSource: FirebaseMessageDataSource
): FirebaseMessagingRepository {
    override suspend fun getFcmToken(): String = firebaseMessageDataSource.getFcmToken()
    override suspend fun updateNewFcmToken(token: String) {
        firebaseMessageDataSource.updateNewFcmToken(token)
    }
}