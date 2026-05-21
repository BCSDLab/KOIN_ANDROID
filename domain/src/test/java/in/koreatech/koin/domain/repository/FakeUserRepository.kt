package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.ABTest
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.CodeCount
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType
import kotlin.Result
import kotlinx.coroutines.flow.Flow

class FakeUserRepository : UserRepository {
    private var authToken: AuthToken? = null
    private var user: User? = null
    var userType: UserType = UserType.ANONYMOUS

    fun getUpdatedUser(): User? = user

    fun setAccessToken(token: AuthToken?) {
        authToken = token
    }

    override suspend fun getToken(email: String, password: String): Result<AuthToken> = authToken?.let { Result.success(it) } ?: Result.failure(IllegalStateException("No access token set"))

    override suspend fun getOwnerToken(phoneNumber: String, hashedPassword: String): AuthToken {
        TODO("Not yet implemented")
    }

    override fun ownerTokenIsValid(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun fetchStudentUserInfo() {
        userType = UserType.STUDENT
    }

    override suspend fun fetchGeneralUserInfo() {
        userType = UserType.GENERAL
    }

    override suspend fun getUserInfo(): User {
        TODO("Not yet implemented")
    }

    override fun getUserInfoFlow(): Flow<User> {
        TODO("Not yet implemented")
    }

    override suspend fun requestPasswordResetEmail(email: String): Result<Unit> {
        return Result.failure(NotImplementedError())
    }

    override suspend fun deleteUser(): Result<Unit> {
        return Result.failure(NotImplementedError())
    }

    override suspend fun isUserEmailDuplicated(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(user: User): Result<Unit> = when (user) {
        is User.Student -> {
            this.user = user
            Result.success(Unit)
        }
        is User.General -> TODO()
        User.Anonymous -> TODO()
    }

    override suspend fun deleteDeviceToken(): Result<Unit> {
        return Result.failure(NotImplementedError())
    }

    override suspend fun verifyPassword(hashedPassword: String): Result<Unit> {
        return Result.failure(NotImplementedError())
    }

    override suspend fun updateUserPassword(hashedPassword: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun updateABTestToken() {
        TODO("Not yet implemented")
    }

    override suspend fun postABTestAssign(title: String): ABTest {
        TODO("Not yet implemented")
    }

    override suspend fun getCachedABTest(title: String): ABTest {
        TODO("Not yet implemented")
    }

    override suspend fun requestSmsVerification(phoneNumber: String): Result<CodeCount> {
        TODO("Not yet implemented")
    }

    override suspend fun requestEmailVerification(email: String): Result<CodeCount> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyCertificationCode(phoneNumber: String, verificationCode: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyEmailCode(email: String, verificationCode: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIdExists(loginId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIdMatchEmail(loginId: String, email: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun checkIdMatchPhone(loginId: String, phone: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPasswordByEmail(loginId: String, email: String, newPassword: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun resetPasswordBySms(loginId: String, phone: String, newPassword: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun checkEmailExists(email: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun checkPhoneExists(phone: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun findLoginIdByEmail(email: String, verificationCode: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun findLoginIdBySms(phone: String, verificationCode: String): Result<String> {
        TODO("Not yet implemented")
    }
}
