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
import `in`.koreatech.koin.data.util.getErrorResponse
import `in`.koreatech.koin.data.util.toKoinUnknownErrorException
import `in`.koreatech.koin.domain.error.KoinUnknownErrorException
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

    override suspend fun updateUser(user: User) {
        runCatching {
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
        }.onFailure {
            throw if (it is HttpException) {
                when (it.code()) {
                    400 -> KoinUserException.DataInvalidException()
                    401 -> KoinUserException.UnauthorizedException()
                    404 -> KoinUserException.UserNotFoundException()
                    409 -> KoinUserException.NicknameOrEmailConflictException()
                    else -> it.getErrorResponse().let { errorResponse ->
                        KoinUnknownErrorException(errorResponse.code, errorResponse.message, errorResponse.errorTraceId)
                    }
                }
            } else {
                it
            }
        }
    }

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
    ) {
        userRemoteDataSource.updateUserPassword(hashedPassword) // TODO: Handle error after error code PR is completed.
    }

    override suspend fun requestSmsVerification(phoneNumber: String): Result<CodeCount> {
        return runCatching {
            userRemoteDataSource.sendSMS(SmsSendRequest(phoneNumber)).toCodeCount()
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            429 -> KoinUserException.VerificationCodeRequestCountExceededException()
                            400 -> KoinUserException.PhoneNumberInvalidException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun requestEmailVerification(email: String): Result<CodeCount> {
        return runCatching {
            userRemoteDataSource.sendEmail(EmailSendRequest(email)).toCodeCount()
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            429 -> KoinUserException.VerificationCodeRequestCountExceededException()
                            400 -> KoinUserException.EmailInvalidException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> throw exception
                }
            )
        }
    }

    override suspend fun verifyCertificationCode(phoneNumber: String, verificationCode: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.verifyCode(
                SmsVerifyRequest(
                    phoneNumber = phoneNumber,
                    verificationCode = verificationCode
                )
            )
        }.onFailure {
            return Result.failure(
                when (it) {
                    is HttpException -> {
                        when (it.code()) {
                            404 -> KoinUserException.VerificationCodeExpiredException()
                            400 -> KoinUserException.VerificationCodeInvalidException()
                            else -> it.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> it
                }
            )
        }
    }

    override suspend fun verifyEmailCode(email: String, verificationCode: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.verifyEmailCode(
                EmailVerifyRequest(
                    email = email,
                    verificationCode = verificationCode
                )
            )
        }.onFailure {
            return Result.failure(
                when (it) {
                    is HttpException -> {
                        when (it.code()) {
                            404 -> KoinUserException.VerificationCodeExpiredException()
                            400 -> KoinUserException.VerificationCodeInvalidException()
                            else -> it.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> it
                }
            )
        }
    }

    override suspend fun checkIdExists(loginId: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.idExists(loginId)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            404 -> KoinUserException.LoginIdNotFoundException()
                            400 -> KoinUserException.LoginIdInvalidException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun checkIdMatchEmail(loginId: String, email: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.idMatchEmail(loginId, email)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            404 -> KoinUserException.LoginIdNotFoundException()
                            400 -> KoinUserException.LoginIdNotMatchEmailException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun checkIdMatchPhone(loginId: String, phone: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.idMatchPhone(
                loginId,
                phone
            )
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            404 -> KoinUserException.LoginIdNotFoundException()
                            400 -> KoinUserException.LoginIdNotMatchPhoneException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun resetPasswordByEmail(loginId: String, email: String, newPassword: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.resetPasswordByEmail(
                loginId,
                email,
                newPassword
            )
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            404 -> KoinUserException.LoginIdNotFoundException()
                            400 -> KoinUserException.LoginIdNotMatchEmailException()
                            401 -> KoinUserException.UnauthorizedException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun resetPasswordBySms(loginId: String, phone: String, newPassword: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.resetPasswordBySms(loginId, phone, newPassword)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            404 -> KoinUserException.LoginIdNotFoundException()
                            400 -> KoinUserException.LoginIdNotMatchPhoneException()
                            401 -> KoinUserException.UnauthorizedException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun checkEmailExists(email: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.checkEmailExists(email)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> when (exception.code()) {
                        400 -> KoinUserException.EmailInvalidException()
                        404 -> KoinUserException.EmailNotFoundException()
                        else -> exception.getErrorResponse().toKoinUnknownErrorException()
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun checkPhoneExists(phone: String): Result<Unit> {
        return runCatching {
            userRemoteDataSource.checkPhoneExists(phone)
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> when (exception.code()) {
                        400 -> KoinUserException.PhoneNumberInvalidException()
                        404 -> KoinUserException.PhoneNumberNotFoundException()
                        else -> exception.getErrorResponse().toKoinUnknownErrorException()
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun findLoginIdByEmail(email: String, verificationCode: String): Result<String> {
        return runCatching {
            userRemoteDataSource.findLoginIdByEmail(EmailVerifyRequest(email, verificationCode)).loginId
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinUserException.EmailInvalidException()
                            401 -> KoinUserException.UnauthorizedException()
                            404 -> KoinUserException.EmailNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }

    override suspend fun findLoginIdBySms(phone: String, verificationCode: String): Result<String> {
        return runCatching {
            userRemoteDataSource.findLoginIdBySms(SmsVerifyRequest(phone, verificationCode)).loginId
        }.onFailure { exception ->
            return Result.failure(
                when (exception) {
                    is HttpException -> {
                        when (exception.code()) {
                            400 -> KoinUserException.PhoneNumberInvalidException()
                            401 -> KoinUserException.UnauthorizedException()
                            404 -> KoinUserException.PhoneNumberNotFoundException()
                            else -> exception.getErrorResponse().toKoinUnknownErrorException()
                        }
                    }

                    else -> exception
                }
            )
        }
    }
}
