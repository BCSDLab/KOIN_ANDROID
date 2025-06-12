package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.ABTest
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.VerificationCode
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getToken(
        loginId: String,
        hashedPassword: String
    ): AuthToken

    suspend fun getOwnerToken(
        phoneNumber: String,
        hashedPassword: String
    ): AuthToken

    fun ownerTokenIsValid(): Boolean

    suspend fun fetchStudentUserInfo()

    suspend fun fetchGeneralUserInfo()

    suspend fun getUserInfo(): User

    fun getUserInfoFlow(): Flow<User>

    suspend fun requestPasswordResetEmail(email: String)

    suspend fun deleteUser()

    suspend fun isUsernameDuplicated(nickname: String): Boolean

    suspend fun isUserEmailDuplicated(email: String): Boolean // TODO: Remove after new sign up release

    suspend fun updateUser(user: User)

    suspend fun deleteDeviceToken()

    suspend fun verifyPassword(hashedPassword: String)

    suspend fun updateUserPassword(
        user: User,
        hashedPassword: String
    )

    suspend fun updateABTestToken()

    suspend fun postABTestAssign(title: String): ABTest

    suspend fun requestSmsVerification(phoneNumber: String): PhoneNumber

    suspend fun requestEmailVerification(email: String): PhoneNumber

    suspend fun verifyCertificationCode(phoneNumber: String, verificationCode: String): VerificationCode

    suspend fun verifyEmailCode(email: String, verificationCode: String): VerificationCode

    suspend fun checkIdExists(loginId: String): Result<Unit>

    suspend fun checkIdMatchEmail(loginId: String, email: String): Result<Unit>

    suspend fun checkIdMatchPhone(loginId: String, phone: String): Result<Unit>

    suspend fun passwordResetByEmail(
        loginId: String,
        email: String,
        newPassword: String
    ): Result<Unit>

    suspend fun passwordResetBySms(
        loginId: String,
        phone: String,
        newPassword: String
    ): Result<Unit>
}
