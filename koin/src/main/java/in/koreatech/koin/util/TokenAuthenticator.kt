package `in`.koreatech.koin.util

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.widget.Toast
import androidx.core.os.HandlerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import `in`.koreatech.koin.R
import `in`.koreatech.koin.data.api.refresh.UserRefreshApi
import `in`.koreatech.koin.data.request.user.RefreshTokenRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.ui.login.LoginActivity
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.HttpURLConnection
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userRefreshApi: UserRefreshApi
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        try {
            if (response.code() != HttpURLConnection.HTTP_UNAUTHORIZED) return null

            val refreshToken = runBlocking { tokenLocalDataSource.getRefreshToken() }
            if(refreshToken == null) {
                goToLoginActivity()
                return null
            }

            val (newAccessToken, newRefreshToken) = runBlocking {
                userRefreshApi.refreshAccessToken(RefreshTokenRequest(refreshToken))
            }
            runBlocking {
                tokenLocalDataSource.saveAccessToken(newAccessToken)
                tokenLocalDataSource.saveRefreshToken(newRefreshToken)
            }

            if ("Bearer $newAccessToken" == response.request().header("Authorization")) {
                runBlocking {
                    tokenLocalDataSource.removeAccessToken()
                }
                goToLoginActivity()
                return null
            }

            return getRequest(response, newAccessToken)
        } catch (e: Exception) {
            //goToLoginActivity()
            return null
        }
    }

    private fun getRequest(response: Response, token: String): Request {
        return response.request()
            .newBuilder()
            .header("Authorization", "Bearer $token")
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

    companion object {
        const val TAG = "TokenAuthenticator"
    }
}