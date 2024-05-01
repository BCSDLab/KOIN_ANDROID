package `in`.koreatech.koin.di.network

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.mapper.toAuthToken
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.domain.usecase.user.DeleteUserRefreshTokenUseCase
import `in`.koreatech.koin.domain.usecase.user.UpdateUserRefreshTokenUseCase
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
    private val updateUserRefreshTokenUseCase: UpdateUserRefreshTokenUseCase,
    private val deleteUserRefreshTokenUseCase: DeleteUserRefreshTokenUseCase,
    private val userApi: UserApi,
    private val maxRetry: Int = 5,
) : Authenticator {
    private val mutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        mutex.withLock {
            if (response.responseCount() > maxRetry) {
                deleteUserRefreshTokenUseCase()
                return@withLock null
            }
            val currentToken = tokenLocalDataSource.getRefreshToken() ?: ""

            val newResponse = runCatching {
                userApi.postUserRefresh(RefreshRequest(currentToken))
            }.getOrNull()

            val tokenBody = newResponse?.body()?.toAuthToken() ?: run {
                deleteUserRefreshTokenUseCase()
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

}