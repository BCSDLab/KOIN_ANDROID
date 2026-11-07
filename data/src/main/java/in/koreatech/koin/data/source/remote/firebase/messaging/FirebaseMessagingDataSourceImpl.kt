package `in`.koreatech.koin.data.source.remote.firebase.messaging

import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.user.DeviceTokenRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import java.util.concurrent.Executor
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber

class FirebaseMessagingDataSourceImpl @Inject constructor(
    private val messaging: FirebaseMessaging,
    private val userAuthApi: UserAuthApi,
    private val tokenLocalDataSource: TokenLocalDataSource
) : FirebaseMessageDataSource {
    override suspend fun getFcmToken(): String =
        messaging.token
            .awaitResult { exception ->
                Timber.e("Fcm getToken Failed : ${exception.message}")
            }.also { token ->
                Timber.e("Success getToken : $token")
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

private val inlineExecutor = Executor { runnable -> runnable.run() }

/**
 * Awaits a Google Play services [Task] and completes the caller exactly once.
 *
 * A successful task resumes with its result, a failed task resumes with the original
 * cause, and a canceled task resumes with [CancellationException]. If the caller is
 * canceled first, its continuation is already completed, so a task completing later
 * is ignored instead of resuming the caller a second time.
 *
 * @param onFailure invoked with the failure cause before it is propagated to the caller.
 */
internal suspend fun <T> Task<T>.awaitResult(onFailure: (Exception) -> Unit = {}): T =
    suspendCancellableCoroutine { continuation ->
        addOnCompleteListener(inlineExecutor) { completedTask ->
            val exception = completedTask.exception
            when {
                completedTask.isCanceled ->
                    continuation.resumeWithException(
                        CancellationException("Firebase task was canceled")
                    )

                exception != null -> {
                    onFailure(exception)
                    continuation.resumeWithException(exception)
                }

                else -> continuation.resume(completedTask.result)
            }
        }
    }
