package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.UserApi
import `in`.koreatech.koin.data.api.auth.UserAuthApi
import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.request.user.ABTestRequest
import `in`.koreatech.koin.data.request.user.DeviceTokenRequest
import `in`.koreatech.koin.data.request.user.GeneralInfoRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.request.user.SmsVerifyRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest_V2
import `in`.koreatech.koin.data.request.user.UserRequest
import `in`.koreatech.koin.data.response.owner.OwnerAuthResponse
import `in`.koreatech.koin.data.response.user.ABTestResponse
import `in`.koreatech.koin.data.response.user.ABTestTokenResponse
import `in`.koreatech.koin.data.response.user.AuthResponse
import `in`.koreatech.koin.data.response.user.TokenResponse
import `in`.koreatech.koin.data.response.user.UserResponse
import `in`.koreatech.koin.data.response.user.UserTypeResponse
import retrofit2.Response

class UserRemoteDataSource(
    private val userApi: UserApi,
    private val userAuthApi: UserAuthApi
) {
    suspend fun getToken(loginRequest: LoginRequest): AuthResponse {
        return userApi.getToken(loginRequest)
    }

    suspend fun getOwnerToken(ownerLoginRequest: OwnerLoginRequest): OwnerAuthResponse {
        return userApi.getOwnerToken(ownerLoginRequest)
    }

    suspend fun ownerTokenIsValid() {
        userAuthApi.getOwnerTokenIsValid()
    }

    suspend fun getUserInfo(): UserResponse {
        return userAuthApi.getUser()
    }

    suspend fun sendRegisterEmail(studentInfoRequest: StudentInfoRequest) {
        userApi.postRegister(studentInfoRequest)
    }

    suspend fun sendPasswordResetEmail(idRequest: IdRequest) {
        return userApi.postPasswordReset(idRequest)
    }

    suspend fun deleteUser() {
        userAuthApi.deleteUser()
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

    suspend fun updateABTestToken(): ABTestTokenResponse {
        return userAuthApi.updateABTestToken()
    }

    suspend fun postABTestAssign(abTestRequest: ABTestRequest): ABTestResponse {
        return userAuthApi.postABTestAssign(abTestRequest)
    }

    suspend fun getUserType(): UserTypeResponse {
        return userAuthApi.getUserType()
    }

    suspend fun checkPhoneNumberDuplicate(phone: String) {
        userApi.checkPhoneNumberDuplicate(phone)
    }

    suspend fun checkNickname_V2(nickname: String) {
        userApi.checkNickname_V2(nickname)
    }

    suspend fun postStudentRegister(token:String, studentInfoRequest: StudentInfoRequest_V2) {
        userApi.postStudentRegister(token, studentInfoRequest)
    }

    suspend fun postGeneralRegister(generalInfoRequest: GeneralInfoRequest) {
        userApi.postGeneralRegister(generalInfoRequest)
    }

    suspend fun sendSMS(phoneNumber: String) {
        userApi.smsSend(phoneNumber)
    }

    suspend fun verifyCode(smsVerifyRequest: SmsVerifyRequest) : TokenResponse {
        return userAuthApi.codeVerify(smsVerifyRequest)
    }
}
