package `in`.koreatech.koin.di.network

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userApi: UserApi,
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val currentToken = runBlocking {
            tokenLocalDataSource.getRefreshToken()
        } ?: ""

        return synchronized(this) {
            val newResponse = runBlocking {
                try {
                    userApi.postUserRefresh(RefreshRequest(currentToken))
                } catch (e: Exception) {
                    null
                }
            }

            val token = if (newResponse?.isSuccessful == true) {
                runBlocking {
                    newResponse.body()?.let { responseBody ->
                        tokenLocalDataSource.saveAccessToken(responseBody.token)
                        tokenLocalDataSource.saveRefreshToken(responseBody.refreshToken)
                        responseBody.token
                    }
                }
            } else {
                runBlocking {
                    tokenLocalDataSource.removeAccessToken()
                    tokenLocalDataSource.removeRefreshToken()
                }
                null
            }

            if (token != null) {
                response.request.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                null
            }
        }
    }
}