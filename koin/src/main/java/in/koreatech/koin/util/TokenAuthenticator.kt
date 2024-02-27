package `in`.koreatech.koin.util

import `in`.koreatech.koin.R
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.ui.login.LoginActivity
import android.content.Context
import android.content.Intent
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.os.HandlerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.HttpURLConnection

class TokenAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenLocalDataSource: TokenLocalDataSource
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val request = try {
            if (response.code != HttpURLConnection.HTTP_UNAUTHORIZED) null
            else {
                val accessToken = tokenLocalDataSource.getAccessToken()
                if("Bearer $accessToken" == response.request.header("Authorization")) {
                    tokenLocalDataSource.removeAccessToken()
                    goToLoginActivity()
                    null
                } else {
                    if (accessToken.isNullOrEmpty()) {
                        goToLoginActivity()
                        null
                    } else {
                        getRequest(response, accessToken)
                    }
                }
            }
        } catch (e: Exception) {
            goToLoginActivity()
            null
        }

        request
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

    companion object {
        const val TAG = "TokenAuthenticator"
    }
}