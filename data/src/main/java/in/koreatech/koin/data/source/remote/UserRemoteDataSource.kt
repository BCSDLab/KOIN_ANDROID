package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.RegisterResponse
import `in`.koreatech.koin.data.response.user.UserResponse

class UserRemoteDataSource(
    private val userApi: UserApi,
    private val userAuthApi: UserAuthApi
) {
    suspend fun getToken(
        loginRequest: LoginRequest
    ): AuthResponse {
        return userApi.getToken(loginRequest)
    }

    suspend fun getUserInfo(): UserResponse {
        return userAuthApi.getUser()
    }

    suspend fun sendRegisterEmail(
        loginRequest: LoginRequest
    ): RegisterResponse {
        return userApi.postRegister(loginRequest)
    }
}