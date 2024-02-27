package `in`.koreatech.koin.util

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.widget.Toast
import androidx.core.os.HandlerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.R
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.ui.login.LoginActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class RefreshTokenInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userApi: UserApi,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        val request = chain.request().newBuilder().build()
        var response = chain.proceed(request)

        try {
            when (response.code) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    val refreshToken = tokenLocalDataSource.getRefreshToken()
                    refreshToken?.let {
                        val result = userApi.postUserRefresh(RefreshRequest(it))
                        if (result.isSuccessful) {
                            result.body()?.let { resultBody ->
                                tokenLocalDataSource.saveAccessToken(resultBody.token)
                                tokenLocalDataSource.saveRefreshToken(resultBody.refreshToken)
                                response = chain.proceed(getRequest(response, resultBody.token))
                            }
                        } else {
                            tokenLocalDataSource.removeAccessToken()
                            tokenLocalDataSource.removeRefreshToken()
                            goToLoginActivity()
                        }
                    }
                }
                else -> Unit
            }
        } catch (e: Exception) {
            goToLoginActivity()
        }

        response
    }

    private fun getRequest(response: Response, token: String): Request {
        return response.request
            .newBuilder()
            .removeHeader("Authorization")
            .addHeader("Authorization", "Bearer $token")
            .build()
    }

    private fun goToLoginActivity() {
        val handler = HandlerCompat.createAsync(Looper.getMainLooper())
        Intent(context.applicationContext, LoginActivity::class.java).run {
            handler.post {
                Toast.makeText(
                    context.applicationContext,
                    context.getString(R.string.token_out_dated),
                    Toast.LENGTH_SHORT
                ).show()
            }
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(this)
        }
    }
}