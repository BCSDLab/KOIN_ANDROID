package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toUser
import `in`.koreatech.koin.data.mapper.toUserRequest
import `in`.koreatech.koin.data.mapper.toUserRequestWithPassword
import `in`.koreatech.koin.data.request.owner.OwnerLoginRequest
import `in`.koreatech.koin.data.request.user.ABTestRequest
import `in`.koreatech.koin.data.request.user.GeneralInfoRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.request.user.SmsVerifyRequest
import `in`.koreatech.koin.data.request.user.StudentInfoRequest_V2
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.local.UserLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.domain.model.user.ABTest
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.Duplicated
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userLocalDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun getToken(
        email: String,
        hashedPassword: String
    ): AuthToken {
        val authResponse =
            userRemoteDataSource.getToken(
                LoginRequest(email, hashedPassword)
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

    override suspend fun fetchUserInfo(userType: String) {
        userRemoteDataSource.getUserInfo().toUser(userType).also {
            userLocalDataSource.updateUserInfo(it)
        }
    }

    override suspend fun getUserInfo(): User {
        val userType = userRemoteDataSource.getUserType()

        return userRemoteDataSource.getUserInfo().toUser(userType.userType).also {
            userLocalDataSource.updateUserInfo(it)
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
        when (user) {
            User.Anonymous -> throw IllegalAccessException("Updating anonymous user is not supported")
            is User.Student -> {
                userRemoteDataSource.updateUser(user.toUserRequest())
                userLocalDataSource.updateUserInfo(user)
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
                userRemoteDataSource.updateUser(user.toUserRequestWithPassword(hashedPassword))
            }
        }
    }

    override suspend fun isUsernameDuplicated_V2(nickname: String): Duplicated {
        return try {
            userRemoteDataSource.checkNickname_V2(nickname)
            Duplicated.OK
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Duplicated.INVALID_FORMAT
                409 -> Duplicated.CONFLICT
                else -> Duplicated.UNDEFINED
            }
        }
    }

    override suspend fun isPhoneDuplicated(phone: String): Duplicated {
        return try {
            userRemoteDataSource.checkPhoneNumberDuplicate(phone)
            Duplicated.OK
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> Duplicated.INVALID_FORMAT
                409 -> Duplicated.CONFLICT
                else -> Duplicated.UNDEFINED
            }
        }
    }

    override suspend fun postStudentRegister(
        token: String,
        name: String,
        phoneNumber: String,
        userId: String,
        password: String,
        department: String,
        studentNumber:String,
        gender: String,
        email: String,
        nickname: String
    ): Boolean {
        return try {
            userRemoteDataSource.postStudentRegister(
                token,
                StudentInfoRequest_V2(
                    name = name,
                    phoneNumber = phoneNumber,
                    userId = userId,
                    password = password,
                    department = department,
                    studentNumber = studentNumber,
                    gender = gender,
                    email = email,
                    nickname = nickname
                )
            )
            true
        } catch (e: HttpException) {
            false
        }
    }

    override suspend fun postGeneralRegister(token: String, name: String, phoneNumber: String, userId: String, password: String, gender: String, email: String, nickname: String): Boolean {
        return try {
            userRemoteDataSource.postGeneralRegister(
                token,
                GeneralInfoRequest(
                    name = name,
                    phoneNumber = phoneNumber,
                    userId = userId,
                    password =  password,
                    gender = gender,
                    email = email,
                    nickname = nickname
                )
            )
            false
        } catch (e: HttpException) {
            false
        }
    }

    override suspend fun sendSMS(phoneNumber: String): Boolean {
        return try {
            userRemoteDataSource.sendSMS(phoneNumber)
            true
        } catch (e: HttpException) {
            false
        }
    }

    override suspend fun verifyCertificationCode(phoneNumber: String, certificationCode: String): String {
        return try {
            userRemoteDataSource.verifyCode(
                SmsVerifyRequest(
                    phoneNumber = phoneNumber,
                    certificationCode = certificationCode
                )
            ).token
        } catch (e: HttpException) {
            ""
        }
    }
}
