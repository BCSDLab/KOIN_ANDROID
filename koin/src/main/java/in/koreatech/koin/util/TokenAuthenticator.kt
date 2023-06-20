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
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.user.RefreshTokenRequest
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.domain.usecase.token.RefreshAccessTokenUseCase
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.net.HttpURLConnection

class TokenAuthenticator @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userAuthApi: UserAuthApi
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        val request = try {
            if (response.code() != HttpURLConnection.HTTP_UNAUTHORIZED) return@runBlocking null

            val refreshToken = tokenLocalDataSource.getRefreshToken()
            if(refreshToken == null) {
                goToLoginActivity()
                return@runBlocking null
            }

            val newAccessToken = userAuthApi.refreshAccessToken(RefreshTokenRequest(refreshToken)).accessToken
            tokenLocalDataSource.saveAccessToken(newAccessToken)

            if ("Bearer $newAccessToken" == response.request().header("Authorization")) {
                tokenLocalDataSource.removeAccessToken()
                goToLoginActivity()
                return@runBlocking null
            }

            getRequest(response, newAccessToken)
        } catch (e: Exception) {
            goToLoginActivity()
            null
        }

        request
    }

    private fun getRequest(response: Response, token: String): Request {
        return response.request()
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