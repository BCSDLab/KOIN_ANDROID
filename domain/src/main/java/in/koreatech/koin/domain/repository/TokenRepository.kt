package `in`.koreatech.koin.domain.repository

interface TokenRepository {
    suspend fun saveAccessToken(token: String)
    suspend fun saveRefreshToken(token: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    fun getAccessTokenBlocking(): String?
    suspend fun removeToken()
    suspend fun saveOwnerAccessToken(token: String)
    suspend fun getOwnerAccessToken(): String?

    fun getAccessOwnerTokenBlocking(): String?
    suspend fun removeOwnerAccessToken()
    suspend fun removeRefreshToken()
}