package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toAuthToken
import `in`.koreatech.koin.data.mapper.toUser
import `in`.koreatech.koin.data.mapper.toUserRequest
import `in`.koreatech.koin.data.request.user.IdRequest
import `in`.koreatech.koin.data.request.user.LoginRequest
import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.model.user.User
import `in`.koreatech.koin.domain.repository.UserRepository
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
    override suspend fun getToken(portalAccount: String, hashedPassword: String): AuthToken {
        val authResponse = userRemoteDataSource.getToken(
            LoginRequest(portalAccount, hashedPassword)
        )

        return authResponse.toAuthToken()
    }
    override suspend fun getUserInfo(): User {
        return userRemoteDataSource.getUserInfo().toUser()
    }

    override suspend fun requestPasswordResetEmail(portalAccount: String) {
        userRemoteDataSource.sendPasswordResetEmail(IdRequest(portalAccount))
    }

    override suspend fun deleteUser() {
        userRemoteDataSource.deleteUser()
    }

    override suspend fun isUsernameDuplicated(nickname: String): Boolean {
        return try {
            userRemoteDataSource.checkNickname(nickname)
            false
        } catch (e: HttpException) {
            if(e.code() == 409) true
            else throw e
        }
    }

    override suspend fun updateUser(user: User) {
        userRemoteDataSource.updateUser(user.toUserRequest())
    }
}