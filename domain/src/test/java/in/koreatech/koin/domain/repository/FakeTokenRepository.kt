package `in`.koreatech.koin.domain.repository

class FakeTokenRepository : TokenRepository {
    private var accessToken: String? = null
    private var refreshToken: String? = null
    private var accessHistoryId: String? = null
    private var ownerAccessToken: String? = null

    override suspend fun saveAccessToken(token: String) {
        accessToken = token
    }

    override suspend fun saveRefreshToken(token: String) {
        refreshToken = token
    }

    override suspend fun saveAccessHistoryId(token: String) {
        accessHistoryId = token
    }

    override suspend fun getAccessToken(): String? = accessToken

    override suspend fun getRefreshToken(): String? = refreshToken

    override suspend fun getAccessHistoryId(): String? = accessHistoryId

    override fun getAccessTokenBlocking(): String? = accessToken

    override suspend fun removeToken() {
        accessToken = null
        refreshToken = null
    }

    override suspend fun saveOwnerAccessToken(token: String) {
        ownerAccessToken = token
    }

    override suspend fun getOwnerAccessToken(): String? = ownerAccessToken

    override fun getAccessOwnerTokenBlocking(): String? = ownerAccessToken

    override suspend fun removeOwnerAccessToken() {
        ownerAccessToken = null
    }

    override suspend fun removeRefreshToken() {
        refreshToken = null
    }
}
