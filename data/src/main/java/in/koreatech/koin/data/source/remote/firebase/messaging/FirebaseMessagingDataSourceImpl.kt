package `in`.koreatech.koin.data.source.remote.firebase.messaging

import com.google.firebase.messaging.FirebaseMessaging
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.user.DeviceTokenRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject

class FirebaseMessagingDataSourceImpl @Inject constructor(
    private val messaging: FirebaseMessaging,
    private val userAuthApi: UserAuthApi,
    private val tokenLocalDataSource: TokenLocalDataSource,
): FirebaseMessageDataSource {
    override suspend fun getFcmToken(): String {
        return suspendCancellableCoroutine { continuation ->
            messaging.token.addOnCompleteListener {
                Timber.e("Success getToken : ${it.result}")
                continuation.resumeWith(Result.success(it.result))
            }.addOnFailureListener {
                Timber.e("Fcm getToken Failed : ${it.message}")
            }
        }
    }

    override suspend fun updateNewFcmToken(token: String) {
        if (token.isNotEmpty() && token != tokenLocalDataSource.getDeviceToken()) {
            tokenLocalDataSource.saveDeviceToken(token)
        }
        if (!tokenLocalDataSource.getAccessToken().isNullOrEmpty()) {
            userAuthApi.updateDeviceToken(DeviceTokenRequest(token))
        }
    }
}