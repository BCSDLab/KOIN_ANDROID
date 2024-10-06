package `in`.koreatech.koin.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.domain.repository.NotificationRepository
import `in`.koreatech.koin.domain.repository.firebase.messaging.FirebaseMessagingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class KoinFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var firebaseMessagingRepository: FirebaseMessagingRepository

    @IoDispatcher
    @Inject
    lateinit var coroutineDispatcher: CoroutineDispatcher
    private val coroutineScope by lazy { CoroutineScope(coroutineDispatcher) }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        coroutineScope.launch {
            try {
                Timber.e("Success Fcm NewToken : $token")
                if (token.isNotEmpty()) {
                    firebaseMessagingRepository.updateNewFcmToken(token)
                }
            } catch (e: Exception) {
                Timber.e("Failed Fcm NewToken : ${e.message}")
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.data.let { data ->
            Timber.e("FirebaseMessaging Received Data Payload : $data")
            if (data.isNotEmpty()) {

            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}