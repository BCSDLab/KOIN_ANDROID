package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toUser
import `in`.koreatech.koin.data.mapper.toUserRequest
import `in`.koreatech.koin.data.mapper.toUserRequestWithPassword
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
import `in`.koreatech.koin.domain.error.KoinUnknownErrorException
import `in`.koreatech.koin.domain.error.user.PutUserNicknameOrEmailConflict
import `in`.koreatech.koin.domain.error.user.PutUserNotFound
import `in`.koreatech.koin.domain.error.user.PutUserPhoneNumberNotAuthorized
import `in`.koreatech.koin.domain.error.user.PutUserRequestDataError
import `in`.koreatech.koin.domain.error.user.KoinUserError
import `in`.koreatech.koin.domain.model.user.ABTest
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.PhoneNumber
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.model.user.VerificationCode
import `in`.koreatech.koin.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import javax.inject.Inject

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

            else -> User.Anonymous
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
                    400 -> PutUserRequestDataError()
                    401 -> PutUserPhoneNumberNotAuthorized()
                    404 -> PutUserNotFound()
                    409 -> PutUserNicknameOrEmailConflict()
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
        user: User,
        hashedPassword: String
    ) {
        when (user) {
            User.Anonymous -> throw IllegalAccessException("Updating anonymous user is not supported")
            is User.Student -> {
                userRemoteDataSource.updateStudentUser(user.toUserRequestWithPassword(hashedPassword))
            }

            is User.General -> {
                userRemoteDataSource.updateGeneralUser(user.toUserRequestWithPassword(hashedPassword))
            }
        }
    }


    override suspend fun requestSmsVerification(phoneNumber: String): PhoneNumber {
        return try {
            userRemoteDataSource.sendSMS(SmsSendRequest(phoneNumber = phoneNumber)).let {
                PhoneNumber.Sent(
                    totalCount = it.totalCount,
                    remainingCount = it.remainingCount,
                    currentCount = it.currentCount
                )
            }
        } catch (e: HttpException) {
            when (e.code()) {
                429 -> PhoneNumber.CountExceeded
                400 -> PhoneNumber.WrongFormat
                else -> PhoneNumber.Failed(
                    message = e.getErrorResponse().message ?: "",
                    throwable = e
                )
            }
        }
    }

    override suspend fun requestEmailVerification(email: String): PhoneNumber {
        return try {
            userRemoteDataSource.sendEmail(EmailSendRequest(email)).let {
                PhoneNumber.Sent(
                    totalCount = it.totalCount,
                    remainingCount = it.remainingCount,
                    currentCount = it.currentCount
                )
            }
        } catch (e: HttpException) {
            when (e.code()) {
                429 -> PhoneNumber.CountExceeded
                400 -> PhoneNumber.WrongFormat
                else -> PhoneNumber.Failed(
                    message = e.getErrorResponse().message ?: "",
                    throwable = e
                )
            }
        }
    }

    override suspend fun verifyCertificationCode(phoneNumber: String, verificationCode: String): VerificationCode {
        return try {
            userRemoteDataSource.verifyCode(
                SmsVerifyRequest(
                    phoneNumber = phoneNumber,
                    verificationCode = verificationCode
                )
            )
            VerificationCode.Valid
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> VerificationCode.Expired
                else -> VerificationCode.NotValid
            }
        }
    }

    override suspend fun verifyEmailCode(email: String, verificationCode: String): VerificationCode {
        return try {
            userRemoteDataSource.verifyEmailCode(
                EmailVerifyRequest(
                    email = email,
                    verificationCode = verificationCode
                )
            )
            VerificationCode.Valid
        } catch (e: HttpException) {
            when (e.code()) {
                404 -> VerificationCode.Expired
                else -> VerificationCode.NotValid
            }
        }
    }

    override suspend fun checkIdExists(loginId: String): Result<Unit> {
        return try {
            userRemoteDataSource.idExists(loginId)
            Result.success(Unit)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw KoinUserError.LoginIdNotExists
                        400 -> throw KoinUserError.LoginIdWrongFormat
                        else -> throw KoinUnknownErrorException(
                            code = e.code().toString(),
                            errorMessage = e.getErrorResponse().message,
                            errorTraceId = e.getErrorResponse().errorTraceId
                        )
                    }
                }

                else -> throw e
            }
        }
    }

    override suspend fun checkIdMatchEmail(loginId: String, email: String): Result<Unit> {
        return try {
            userRemoteDataSource.idMatchEmail(
                loginId, email
            )
            Result.success(Unit)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw KoinUserError.LoginIdNotExists
                        400 -> throw KoinUserError.LoginIdNotMatchEmail
                        else -> throw KoinUnknownErrorException(
                            code = e.code().toString(),
                            errorMessage = e.getErrorResponse().message,
                            errorTraceId = e.getErrorResponse().errorTraceId
                        )
                    }
                }

                else -> throw e
            }
        }
    }

    override suspend fun checkIdMatchPhone(loginId: String, phone: String): Result<Unit> {
        return try {
            userRemoteDataSource.idMatchPhone(
                loginId, phone
            )
            Result.success(Unit)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw KoinUserError.LoginIdNotExists
                        400 -> throw KoinUserError.LoginIdNotMatchPhone
                        else -> throw KoinUnknownErrorException(
                            code = e.code().toString(),
                            errorMessage = e.getErrorResponse().message,
                            errorTraceId = e.getErrorResponse().errorTraceId
                        )
                    }
                }

                else -> throw e
            }
        }
    }

    override suspend fun passwordResetByEmail(loginId: String, email: String, newPassword: String): Result<Unit> {
        return try {
            userRemoteDataSource.passwordResetByEmail(
                loginId, email, newPassword
            )
            Result.success(Unit)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw KoinUserError.LoginIdNotExists
                        400 -> throw KoinUserError.LoginIdNotMatchEmail
                        401 -> throw KoinUserError.Unauthorized
                        else -> throw KoinUnknownErrorException(
                            code = e.code().toString(),
                            errorMessage = e.getErrorResponse().message,
                            errorTraceId = e.getErrorResponse().errorTraceId
                        )
                    }
                }

                else -> throw e
            }
        }
    }

    override suspend fun passwordResetBySms(loginId: String, phone: String, newPassword: String): Result<Unit> {
        return try {
            userRemoteDataSource.passwordResetBySms(
                loginId, phone, newPassword
            )
            Result.success(Unit)
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> throw KoinUserError.LoginIdNotExists
                        400 -> throw KoinUserError.LoginIdNotMatchPhone
                        401 -> throw KoinUserError.Unauthorized
                        else -> throw KoinUnknownErrorException(
                            code = e.code().toString(),
                            errorMessage = e.getErrorResponse().message,
                            errorTraceId = e.getErrorResponse().errorTraceId
                        )
                    }
                }

                else -> throw e
            }
        }
    }
}
