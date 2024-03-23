package `in`.koreatech.koin.di.network

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.mapper.toAuthToken
import `in`.koreatech.koin.data.request.user.RefreshRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.domain.usecase.user.GetUserRefreshTokenUseCase
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
    private val getUserRefreshTokenUseCase: GetUserRefreshTokenUseCase,
    private val userApi: UserApi,
) : Authenticator {
    private val mutex = Mutex()
    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        mutex.withLock {
            val currentToken = tokenLocalDataSource.getRefreshToken() ?: ""

            val newResponse = runCatching {
                userApi.postUserRefresh(RefreshRequest(currentToken))
            }.getOrNull()

            val token = newResponse?.body()?.toAuthToken()?.let {
                getUserRefreshTokenUseCase.invoke(
                    isResponseSuccess = newResponse.isSuccessful,
                    refreshBody = it
                )
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