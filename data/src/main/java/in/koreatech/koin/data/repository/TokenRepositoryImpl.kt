package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.remote.UserRemoteDataSource
import `in`.koreatech.koin.domain.model.user.AuthToken
import `in`.koreatech.koin.domain.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : TokenRepository {

    override suspend fun saveAccessToken(accessToken: String) {
        tokenLocalDataSource.saveAccessToken(accessToken)
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        tokenLocalDataSource.saveRefreshToken(refreshToken)
    }

    override suspend fun getAccessToken(): String? {
        return tokenLocalDataSource.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? {
        return tokenLocalDataSource.getRefreshToken()
    }

    override fun getAccessTokenBlocking(): String? {
        return runBlocking { tokenLocalDataSource.getAccessToken() }
    }

    override suspend fun removeAccessToken() {
        tokenLocalDataSource.removeAccessToken()
    }

    override suspend fun removeRefreshToken() {
        tokenLocalDataSource.removeRefreshToken()
    }
}