package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.domain.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource
) : TokenRepository {
    override suspend fun saveAccessToken(token: String) {
        tokenLocalDataSource.saveAccessToken(token)
    }

    override suspend fun saveRefreshToken(token: String) {
        tokenLocalDataSource.saveRefreshToken(token)
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

    override suspend fun removeToken() {
        tokenLocalDataSource.removeAccessToken()
    }

    override suspend fun removeRefreshToken() {
        tokenLocalDataSource.removeRefreshToken()
    }
}