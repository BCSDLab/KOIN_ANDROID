package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.TokenLocalDataSource
import `in`.koreatech.koin.data.source.datastore.UserDataStore
import `in`.koreatech.koin.domain.repository.TokenRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val userDataStore: UserDataStore
) : TokenRepository {
    override suspend fun saveAccessToken(token: String) {
        tokenLocalDataSource.saveAccessToken(token)
        userDataStore.updateIsLogin(true)
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
        userDataStore.updateIsLogin(false)
    }

    override suspend fun saveOwnerAccessToken(token: String) {
        tokenLocalDataSource.saveOwnerAccessToken(token)
    }

    override suspend fun getOwnerAccessToken(): String? {
        return tokenLocalDataSource.getOwnerAccessToken()
    }

    override fun getAccessOwnerTokenBlocking(): String? {
        return runBlocking { tokenLocalDataSource.getOwnerAccessToken() }
    }

    override suspend fun removeOwnerAccessToken() {
        tokenLocalDataSource.removeOwnerAccessToken()
    }

    override suspend fun removeRefreshToken() {
        tokenLocalDataSource.removeRefreshToken()
    }
}