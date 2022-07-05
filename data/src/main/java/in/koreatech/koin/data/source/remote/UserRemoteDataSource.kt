package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.response.AuthResponse

class UserRemoteDataSource(
    private val userApi: UserApi,
    private val userAuthApi: UserAuthApi
) {
    suspend fun getToken(
        loginRequest: LoginRequest
    ): AuthResponse {
        return userApi.getToken(loginRequest)
    }
}