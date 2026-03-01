package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.user.ABTest
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.CodeCount
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeUserRepository : UserRepository {
    private var authToken: AuthToken? = null
    private var user: User? = null
    var userType: UserType = UserType.ANONYMOUS
    private var nicknameDuplicated: Boolean = false
    private var emailDuplicated: Boolean = false
    private var ownerTokenValid: Boolean = false
    private var fakeABTest: ABTest? = null
    private var fakeCodeCount: CodeCount? = null
    private var fakeLoginId: String = ""

    fun setNicknameDuplicated(isDuplicated: Boolean) {
        nicknameDuplicated = isDuplicated
    }

    fun setEmailDuplicated(isDuplicated: Boolean) {
        emailDuplicated = isDuplicated
    }

    fun setOwnerTokenValid(isValid: Boolean) {
        ownerTokenValid = isValid
    }

    fun setFakeABTest(abTest: ABTest) {
        fakeABTest = abTest
    }

    fun setFakeCodeCount(codeCount: CodeCount) {
        fakeCodeCount = codeCount
    }

    fun setFakeLoginId(loginId: String) {
        fakeLoginId = loginId
    }

    fun getUpdatedUser(): User? = user

    fun setAccessToken(token: AuthToken?) {
        authToken = token
    }

    override suspend fun getToken(email: String, password: String): AuthToken =
        authToken ?: throw IllegalStateException("No access token set")

    override suspend fun getOwnerToken(phoneNumber: String, hashedPassword: String): AuthToken =
        authToken ?: throw IllegalStateException("No auth token set")

    override fun ownerTokenIsValid(): Boolean = ownerTokenValid

    override suspend fun fetchStudentUserInfo() {
        userType = UserType.STUDENT
    }

    override suspend fun fetchGeneralUserInfo() {
        userType = UserType.GENERAL
    }

    override suspend fun getUserInfo(): User =
        user ?: throw IllegalStateException("No user set")

    override fun getUserInfoFlow(): Flow<User> = flow {
        emit(user ?: throw IllegalStateException("No user set"))
    }

    override suspend fun requestPasswordResetEmail(email: String) {}

    override suspend fun deleteUser() {}

    override suspend fun isUsernameDuplicated(nickname: String): Boolean = nicknameDuplicated

    override suspend fun isUserEmailDuplicated(email: String): Boolean = emailDuplicated

    override suspend fun updateUser(user: User): Result<Unit> = when (user) {
        is User.Student -> {
            this.user = user
            Result.success(Unit)
        }
        is User.General -> {
            this.user = user
            Result.success(Unit)
        }
        User.Anonymous -> Result.success(Unit)
    }

    override suspend fun deleteDeviceToken() {}

    override suspend fun verifyPassword(hashedPassword: String) {}

    override suspend fun updateUserPassword(hashedPassword: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun updateABTestToken() {}

    override suspend fun postABTestAssign(title: String): ABTest =
        fakeABTest ?: throw IllegalStateException("No ABTest set")

    override suspend fun getCachedABTest(title: String): ABTest =
        fakeABTest ?: throw IllegalStateException("No ABTest set")

    override suspend fun requestSmsVerification(phoneNumber: String): Result<CodeCount> =
        fakeCodeCount?.let { Result.success(it) }
            ?: Result.success(CodeCount(phoneNumber, 3, 3, 0))

    override suspend fun requestEmailVerification(email: String): Result<CodeCount> =
        fakeCodeCount?.let { Result.success(it) }
            ?: Result.success(CodeCount(email, 3, 3, 0))

    override suspend fun verifyCertificationCode(phoneNumber: String, verificationCode: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun verifyEmailCode(email: String, verificationCode: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun checkIdExists(loginId: String): Result<Unit> = Result.success(Unit)

    override suspend fun checkIdMatchEmail(loginId: String, email: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun checkIdMatchPhone(loginId: String, phone: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun resetPasswordByEmail(loginId: String, email: String, newPassword: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun resetPasswordBySms(loginId: String, phone: String, newPassword: String): Result<Unit> =
        Result.success(Unit)

    override suspend fun checkEmailExists(email: String): Result<Unit> = Result.success(Unit)

    override suspend fun checkPhoneExists(phone: String): Result<Unit> = Result.success(Unit)

    override suspend fun findLoginIdByEmail(email: String, verificationCode: String): Result<String> =
        Result.success(fakeLoginId)

    override suspend fun findLoginIdBySms(phone: String, verificationCode: String): Result<String> =
        Result.success(fakeLoginId)
}
