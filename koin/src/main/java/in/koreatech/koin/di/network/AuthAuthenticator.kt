package `in`.koreatech.koin.di.network

import android.content.Context
import android.content.Intent
import android.net.http.HttpException
import android.os.Looper
import androidx.core.os.HandlerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.R
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.mapper.toAuthToken
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.domain.usecase.user.DeleteUserRefreshTokenUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateUserRefreshTokenUseCase
import `in`.koreatech.koin.ui.login.LoginActivity
import `in`.koreatech.koin.util.ext.showToast
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val context: Context,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val updateUserRefreshTokenUseCase: UpdateUserRefreshTokenUseCase,
    private val deleteUserRefreshTokenUseCase: DeleteUserRefreshTokenUseCase,
    private val userApi: UserApi,
    private val maxRetry: Int = 5,
) : Authenticator {
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        mutex.withLock {
            Timber.e("HTTP 401 response : $response")
            Timber.e("토큰 재발급 요청 시도")
            if (response.responseCount() > maxRetry) {
                deleteUserRefreshTokenUseCase()
                goToLoginActivity()
                return@withLock null
            }

            val currentRefreshToken = tokenLocalDataSource.getRefreshToken() ?: ""

            val newResponse = runCatching {
                userApi.postUserRefresh(RefreshRequest(currentRefreshToken))
            }.onSuccess {
                if (!it.isSuccessful) {
                    Timber.e("Refresh API HTTP Exception : $it")
                    deleteUserRefreshTokenUseCase()
                    goToLoginActivity()
                    return@withLock null
                }
            }.onFailure {
                Timber.e("Refresh 재발급 API 호출 에러 : ${it.message}")
            }.getOrNull()

            val tokenBody = newResponse?.body()?.toAuthToken() ?: run {
                deleteUserRefreshTokenUseCase()
                goToLoginActivity()
                return@withLock null
            }

            updateUserRefreshTokenUseCase(tokenBody)
            response.request.newBuilder()
                .removeHeader("Authorization")
                .addHeader("Authorization", "Bearer ${tokenBody.token}")
                .build()
        }
    }

    private fun Response.responseCount(): Int {
        var response: Response? = this
        var result = 1
        while (response?.priorResponse.also { response = it } != null) {
            result++
        }
        return result
    }

    private fun goToLoginActivity() {
        val handler = HandlerCompat.createAsync(Looper.getMainLooper())
        Intent(context.applicationContext, LoginActivity::class.java).run {
            handler.post { context.applicationContext.showToast(context.getString(R.string.token_out_dated)) }
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(this)
        }
    }
}