package `in`.koreatech.koin.firebase

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import `in`.koreatech.koin.core.notification.Notifier
import dagger.hilt.android.AndroidEntryPoint
import `in`.koreatech.koin.core.navigation.utils.EXTRA_URL
import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.domain.repository.firebase.messaging.FirebaseMessagingRepository
import `in`.koreatech.koin.ui.scheme.SchemeActivity
import `in`.koreatech.koin.ui.splash.SplashActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class KoinFirebaseMessagingService : FirebaseMessagingService() {
    companion object {
        private const val URL = "url"
    }
    @Inject
    lateinit var notifier: Notifier

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
        Timber.e("FirebaseMessaging Received Notification Payload : ${message.notification}")

        message.data.let { data ->
            Timber.e("FirebaseMessaging Received Data Payload : $data")
            if (data.isNotEmpty()) {
                val intent = Intent(this, SchemeActivity::class.java).apply {
                    putExtra(EXTRA_URL, data[URL])
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                notifier.sendNotification(data, intent)
            }
        }
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}