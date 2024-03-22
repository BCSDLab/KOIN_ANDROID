package `in`.koreatech.koin.di.network

import `in`.koreatech.koin.core.qualifier.IoDispatcher
import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userApi: UserApi,
) : Authenticator {
    private val mutex = Mutex()
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        mutex.withLock {
            val currentToken = tokenLocalDataSource.getRefreshToken() ?: ""

            val newResponse = try {
                userApi.postUserRefresh(RefreshRequest(currentToken))
            } catch (e: Exception) {
                null
            }

            val token = if (newResponse?.isSuccessful == true) {
                newResponse.body()?.let { responseBody ->
                    tokenLocalDataSource.saveAccessToken(responseBody.token)
                    tokenLocalDataSource.saveRefreshToken(responseBody.refreshToken)
                    responseBody.token
                }
            } else {
                tokenLocalDataSource.removeAccessToken()
                tokenLocalDataSource.removeRefreshToken()
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