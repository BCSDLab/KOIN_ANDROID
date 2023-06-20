package `in`.koreatech.koin.domain.repository

interface TokenRepository {

    suspend fun saveAccessToken(accessToken: String)
    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    fun getAccessTokenBlocking(): String?
    suspend fun removeAccessToken()
    suspend fun removeRefreshToken()
}