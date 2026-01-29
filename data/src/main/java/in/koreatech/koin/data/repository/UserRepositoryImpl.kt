package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toCodeCount
import `in`.koreatech.koin.data.mapper.toUser
import `in`.koreatech.koin.data.mapper.toUserRequest
import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.request.user.ABTestRequest
import `in`.koreatech.koin.data.request.user.EmailSendRequest
import `in`.koreatech.koin.data.request.user.EmailVerifyRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.request.user.SmsSendRequest
import `in`.koreatech.koin.data.request.user.SmsVerifyRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.local.UserLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.data.util.mapHttpFailure
import `in`.koreatech.koin.domain.error.user.KoinUserException
import `in`.koreatech.koin.domain.model.user.ABTest
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.CodeCount
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.UserType
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun getToken(
        loginId: String,
        hashedPassword: String
    ): AuthToken {
        val authResponse =
            userRemoteDataSource.getToken(
                LoginRequest(loginId, hashedPassword)
            )

        return AuthToken(authResponse.token, authResponse.refreshToken, authResponse.userType)
    }

    override suspend fun getOwnerToken(
        phoneNumber: String,
        hashedPassword: String
    ): AuthToken {
        val authResponse =
            userRemoteDataSource.getOwnerToken(
                OwnerLoginRequest(phoneNumber, hashedPassword)
            )

        return AuthToken(authResponse.token, authResponse.refreshToken)
    }

    override fun ownerTokenIsValid(): Boolean {
        return runBlocking {
            try {
                userRemoteDataSource.ownerTokenIsValid()
                true
            } catch (e: HttpException) {
                if (e.code() == 401) {
                    false
                } else {
                    throw e
                }
            }
        }
    }

    override suspend fun fetchStudentUserInfo() {
        userRemoteDataSource.getStudentUserInfo().toUser().also {
            userLocalDataSource.updateUserInfo(it)
        }
    }

    override suspend fun fetchGeneralUserInfo() {
        userRemoteDataSource.getGeneralUserInfo().toUser().also {
            userLocalDataSource.updateUserInfo(it)
        }
    }

    override suspend fun getUserInfo(): User {
        return when (userLocalDataSource.user.first()) {
            is User.Student -> userRemoteDataSource.getStudentUserInfo().toUser().also {
                userLocalDataSource.updateUserInfo(it)
            }

            is User.General -> userRemoteDataSource.getGeneralUserInfo().toUser().also {
                userLocalDataSource.updateUserInfo(it)
            }

            is User.Anonymous -> User.Anonymous

            null -> {
                when (userLocalDataSource.userType.first()) {
                    UserType.STUDENT,
                    UserType.COUNCIL -> userRemoteDataSource.getStudentUserInfo().toUser().also {
                        userLocalDataSource.updateUserInfo(it)
                    }

                    UserType.GENERAL -> userRemoteDataSource.getGeneralUserInfo().toUser().also {
                        userLocalDataSource.updateUserInfo(it)
                    }

                    else -> User.Anonymous
                }
            }
        }
    }

    override fun getUserInfoFlow(): Flow<User> {
        return userLocalDataSource.user.map { it ?: getUserInfo() }
    }

    override suspend fun requestPasswordResetEmail(email: String) {
        userRemoteDataSource.sendPasswordResetEmail(IdRequest(email))
    }

    override suspend fun deleteUser() {
        try {
            userRemoteDataSource.deleteUser()
            userLocalDataSource.updateUserInfo(User.Anonymous)
            userLocalDataSource.updateIsLogin(false)
            tokenLocalDataSource.removeAccessToken()
            tokenLocalDataSource.removeRefreshToken()
        } catch (e: HttpException) {
            throw e
        }
    }

    override suspend fun isUsernameDuplicated(nickname: String): Boolean {
        return try {
            userRemoteDataSource.checkNickname(nickname)
            false
        } catch (e: HttpException) {
            if (e.code() == 409) {
                true
            } else {
                throw e
            }
        }
    }

    override suspend fun isUserEmailDuplicated(email: String): Boolean {
        return try {
            userRemoteDataSource.checkEmail(email)
            false
        } catch (e: HttpException) {
            if (e.code() == 409) {
                true
            } else {
                throw e
            }
        }
    }

    override suspend fun updateUser(user: User): Result<Unit> = runCatching {
        when (user) {
            User.Anonymous -> throw IllegalAccessException("Updating anonymous user is not supported")
            is User.Student -> {
                userRemoteDataSource.updateStudentUser(user.toUserRequest())
                userLocalDataSource.updateUserInfo(user)
            }

            is User.General -> {
                userRemoteDataSource.updateGeneralUser(user.toUserRequest())
                userLocalDataSource.updateUserInfo(user)
            }
        }
    }.onSuccess {
        userLocalDataSource.updateUserInfo(user)
    }.mapHttpFailure(
        e400 = KoinUserException.DataInvalidException(),
        e401 = KoinUserException.UnauthorizedException(),
        e404 = KoinUserException.UserNotFoundException(),
        e409 = KoinUserException.NicknameOrEmailConflictException()
    )

    override suspend fun deleteDeviceToken() {
        tokenLocalDataSource.removeDeviceToken()
        userRemoteDataSource.deleteDeviceToken()
    }

    override suspend fun verifyPassword(hashedPassword: String) {
        userRemoteDataSource.verifyPassword(PasswordRequest(hashedPassword))
    }

    override suspend fun updateABTestToken() {
        userRemoteDataSource.updateABTestToken().accessHistoryId.also {
            tokenLocalDataSource.saveAccessHistoryId(it)
        }
    }

    override suspend fun postABTestAssign(title: String): ABTest {
        userRemoteDataSource.postABTestAssign(ABTestRequest(title)).let {
            return ABTest(it.variableName, it.accessHistoryId)
        }
    }

    override suspend fun updateUserPassword(
        hashedPassword: String
    ): Result<Unit> {
        return runCatching {
            userRemoteDataSource.updateUserPassword(hashedPassword) // TODO: Handle error after error code PR is completed.
        }
    }

    override suspend fun requestSmsVerification(phoneNumber: String): Result<CodeCount> {
        return runCatching {
            userRemoteDataSource.sendSMS(SmsSendRequest(phoneNumber)).toCodeCount()
        }.mapHttpFailure(
            e400 = KoinUserException.PhoneNumberInvalidException(),
            e429 = KoinUserException.VerificationCodeRequestCountExceededException()
        )
    }

    override suspend fun requestEmailVerification(email: String): Result<CodeCount> {
        return runCatching {
            userRemoteDataSource.sendEmail(EmailSendRequest(email)).toCodeCount()
        }.mapHttpFailure(
            e400 = KoinUserException.EmailInvalidException(),
            e429 = KoinUserException.VerificationCodeRequestCountExceededException()
        )
    }

    override suspend fun verifyCertificationCode(phoneNumber: String, verificationCode: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.verifyCode(
                SmsVerifyRequest(
                    phoneNumber = phoneNumber,
                    verificationCode = verificationCode
                )
            )
        }.mapHttpFailure(
            e400 = KoinUserException.VerificationCodeInvalidException(),
            e404 = KoinUserException.VerificationCodeExpiredException()
        )
    }

    override suspend fun verifyEmailCode(email: String, verificationCode: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.verifyEmailCode(
                EmailVerifyRequest(
                    email = email,
                    verificationCode = verificationCode
                )
            )
        }.mapHttpFailure(
            e400 = KoinUserException.VerificationCodeInvalidException(),
            e404 = KoinUserException.VerificationCodeExpiredException()
        )
    }

    override suspend fun checkIdExists(loginId: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.idExists(loginId)
        }.mapHttpFailure(
            e400 = KoinUserException.LoginIdInvalidException(),
            e404 = KoinUserException.LoginIdNotFoundException()
        )
    }

    override suspend fun checkIdMatchEmail(loginId: String, email: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.idMatchEmail(loginId, email)
        }.mapHttpFailure(
            e400 = KoinUserException.LoginIdNotMatchEmailException(),
            e404 = KoinUserException.LoginIdNotFoundException()
        )
    }

    override suspend fun checkIdMatchPhone(loginId: String, phone: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.idMatchPhone(
                loginId,
                phone
            )
        }.mapHttpFailure(
            e400 = KoinUserException.LoginIdNotMatchPhoneException(),
            e404 = KoinUserException.LoginIdNotFoundException()
        )
    }

    override suspend fun resetPasswordByEmail(loginId: String, email: String, newPassword: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.resetPasswordByEmail(
                loginId,
                email,
                newPassword
            )
        }.mapHttpFailure(
            e400 = KoinUserException.LoginIdNotMatchEmailException(),
            e401 = KoinUserException.UnauthorizedException(),
            e404 = KoinUserException.LoginIdNotFoundException()
        )
    }

    override suspend fun resetPasswordBySms(loginId: String, phone: String, newPassword: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.resetPasswordBySms(loginId, phone, newPassword)
        }.mapHttpFailure(
            e400 = KoinUserException.LoginIdNotMatchPhoneException(),
            e401 = KoinUserException.UnauthorizedException(),
            e404 = KoinUserException.LoginIdNotFoundException()
        )
    }

    override suspend fun checkEmailExists(email: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.checkEmailExists(email)
        }.mapHttpFailure(
            e400 = KoinUserException.EmailInvalidException(),
            e404 = KoinUserException.EmailNotFoundException()
        )
    }

    override suspend fun checkPhoneExists(phone: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.checkPhoneExists(phone)
        }.mapHttpFailure(
            e400 = KoinUserException.PhoneNumberInvalidException(),
            e404 = KoinUserException.PhoneNumberNotFoundException()
        )
    }

    override suspend fun findLoginIdByEmail(email: String, verificationCode: String): Result<String> {
        return runCatching {
            userRemoteDataSource.findLoginIdByEmail(EmailVerifyRequest(email, verificationCode)).loginId
        }.mapHttpFailure(
            e400 = KoinUserException.EmailInvalidException(),
            e401 = KoinUserException.UnauthorizedException(),
            e404 = KoinUserException.EmailNotFoundException()
        )
    }

    override suspend fun findLoginIdBySms(phone: String, verificationCode: String): Result<String> {
        return runCatching {
            userRemoteDataSource.findLoginIdBySms(SmsVerifyRequest(phone, verificationCode)).loginId
        }.mapHttpFailure(
            e400 = KoinUserException.PhoneNumberInvalidException(),
            e401 = KoinUserException.UnauthorizedException(),
            e404 = KoinUserException.PhoneNumberNotFoundException()
        )
    }
}
