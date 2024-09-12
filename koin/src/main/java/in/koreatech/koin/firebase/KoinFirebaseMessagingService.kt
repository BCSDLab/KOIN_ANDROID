package `in`.koreatech.koin.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class KoinFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        /**
         * data payload
         *
         * @param Map<String, String>
         *     ex) "data_title" to "title"
         */
        message.data.let { data ->
            if (data.isNotEmpty()) {
                Log.e("aaa", "notEmpty message.data : $data")
            }
        }

        /**
         * notification payload
         *
         * notification
         * @param title
         * @param body
         *
         * android notification
         * @param title
         * @param body
         * @param click_action -> add manifest intent-filter
         *          ex) notifiaction.clickAction = "LOGIN_ACTIVITY"
         *          <intent-filter>
         *             <action android:name="LOGIN_ACTIVITY"/>
         *             <category android:name="android.intent.category.DEFAULT"/>
         *         </intent-filter>
         * ...
         */
        message.notification?.let { notification ->
            notification.title
            notification.body
            notification.clickAction
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}