package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.request.user.DeviceTokenRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.request.user.UserRequest
import `in`.koreatech.koin.data.response.owner.OwnerAuthResponse
import `in`.koreatech.koin.data.response.user.*

class UserRemoteDataSource(
    private val userApi: UserApi,
    private val userAuthApi: UserAuthApi
) {
    suspend fun getToken(
        loginRequest: LoginRequest
    ): AuthResponse {
        return userApi.getToken(loginRequest)
    }

    suspend fun getOwnerToken(
        ownerLoginRequest: OwnerLoginRequest
    ): OwnerAuthResponse {
        return userApi.getOwnerToken(ownerLoginRequest)
    }

    suspend fun ownerTokenIsValid(){
        userAuthApi.getOwnerTokenIsValid()
    }

    suspend fun getUserInfo(): UserResponse {
        return userAuthApi.getUser()
    }

    suspend fun sendRegisterEmail(
        studentInfoRequest: StudentInfoRequest
    ) {
        userApi.postRegister(studentInfoRequest)
    }

    suspend fun sendPasswordResetEmail(
        idRequest: IdRequest
    ) {
        return userApi.postPasswordReset(idRequest)
    }

    suspend fun deleteUser() {
        return userAuthApi.deleteUser()
    }

    suspend fun checkNickname(nickname: String) {
        userApi.checkNickname(nickname)
    }

    suspend fun checkEmail(email: String) {
        userApi.checkEmail(email)
    }

    suspend fun updateUser(userRequest: UserRequest): UserResponse {
        return userAuthApi.putUser(userRequest)
    }

    suspend fun updateDeviceToken(token: String) {
        userAuthApi.updateDeviceToken(DeviceTokenRequest(token))
    }

    suspend fun deleteDeviceToken() {
        userAuthApi.deleteDeviceToken()
    }

    suspend fun verifyPassword(passwordRequest: PasswordRequest) {
        userAuthApi.checkPassword(passwordRequest)
    }
}