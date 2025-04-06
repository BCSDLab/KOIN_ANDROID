package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.ABTest
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.Duplicated
import `in`.koreatech.koin.domain.model.user.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getToken(
        portalAccount: String,
        hashedPassword: String
    ): AuthToken

    suspend fun getOwnerToken(
        phoneNumber: String,
        hashedPassword: String
    ): AuthToken

    fun ownerTokenIsValid(): Boolean

    suspend fun fetchUserInfo(userType: String)

    suspend fun getUserInfo(): User

    fun getUserInfoFlow(): Flow<User>

    suspend fun requestPasswordResetEmail(email: String)

    suspend fun deleteUser()

    suspend fun isUsernameDuplicated(nickname: String): Boolean

    suspend fun isUserEmailDuplicated(email: String): Boolean

    suspend fun updateUser(user: User)

    suspend fun deleteDeviceToken()

    suspend fun verifyPassword(hashedPassword: String)

    suspend fun updateUserPassword(
        user: User,
        hashedPassword: String
    )

    suspend fun updateABTestToken()

    suspend fun postABTestAssign(title: String): ABTest

    suspend fun isUsernameDuplicatedV2(nickname: String): Duplicated

    suspend fun isPhoneDuplicated(phone: String): Duplicated

    suspend fun postStudentRegister(
        token: String,
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        department: String,
        studentNumber: String,
        gender: String,
        email: String,
        nickname: String
    ): Boolean

    suspend fun postGeneralRegister(
        token: String,
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        gender: String,
        email: String,
        nickname: String
    ): Boolean

    suspend fun sendSMS(phoneNumber: String): Boolean

    suspend fun verifyCertificationCode(phoneNumber: String, certificationCode: String): String
}
