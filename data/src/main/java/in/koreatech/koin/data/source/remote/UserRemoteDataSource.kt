package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.UserRequest
import `in`.koreatech.koin.data.response.user.*
import retrofit2.HttpException

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

    suspend fun sendPasswordResetEmail(
        idRequest: IdRequest
    ): DefaultResponse {
        return userApi.postPasswordReset(idRequest)
    }

    suspend fun deleteUser() {
        return userAuthApi.deleteUser()
    }

    suspend fun checkNickname(nickname: String): CheckNicknameResponse {
        return userApi.checkNickname(nickname)
    }

    suspend fun updateUser(userRequest: UserRequest): UserResponse {
        return userAuthApi.putUser(userRequest)
    }
}