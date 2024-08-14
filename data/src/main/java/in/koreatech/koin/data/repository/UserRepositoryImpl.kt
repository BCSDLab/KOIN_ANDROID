package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toUser
import `in`.koreatech.koin.data.mapper.toUserRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
 import `in`.koreatech.koin.data.source.datastore.UserDataStore
import `in`.koreatech.koin.data.request.user.PasswordRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userDataStore: UserDataStore,
) : UserRepository {
    override suspend fun getToken(email: String, hashedPassword: String): AuthToken {
        val authResponse = userRemoteDataSource.getToken(
            LoginRequest(email, hashedPassword)
        )

        return AuthToken(authResponse.token, authResponse.refreshToken, authResponse.userType)
    }

    override suspend fun getUserInfo(): User {
        return userRemoteDataSource.getUserInfo().toUser().also {
            userDataStore.updateUserInfo(it)
        }
    }

    override fun getUserInfoFlow(): Flow<User> {
        return userDataStore.user.map { it ?: getUserInfo() }
    }

    override suspend fun requestPasswordResetEmail(email: String) {
        userRemoteDataSource.sendPasswordResetEmail(IdRequest(email))
    }

    override suspend fun deleteUser() {
        try {
            userRemoteDataSource.deleteUser()
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
            if (e.code() == 409) true
            else throw e
        }
    }

    override suspend fun isUserEmailDuplicated(email: String): Boolean {
        return try {
            userRemoteDataSource.checkEmail(email)
            false
        } catch (e: HttpException) {
            if (e.code() == 409) true
            else throw e
        }
    }

    override suspend fun updateUser(user: User) {
        when (user) {
            User.Anonymous -> throw IllegalAccessException("Updating anonymous user is not supported")
            is User.Student -> {
                userRemoteDataSource.updateUser(user.toUserRequest())
                userDataStore.updateUserInfo(user)
            }
        }
    }

    override suspend fun updateDeviceToken(token: String) {
        if (tokenLocalDataSource.getDeviceToken()
                .isNullOrEmpty() || (tokenLocalDataSource.getDeviceToken() != token && token.isNotEmpty())
        ) {
            tokenLocalDataSource.saveDeviceToken(token)
            userRemoteDataSource.updateDeviceToken(token)
        }
    }

    override suspend fun deleteDeviceToken() {
        tokenLocalDataSource.removeDeviceToken()
        userRemoteDataSource.deleteDeviceToken()
    }

    override suspend fun verifyPassword(hashedPassword: String) {
        userRemoteDataSource.verifyPassword(PasswordRequest(hashedPassword))
    }
}