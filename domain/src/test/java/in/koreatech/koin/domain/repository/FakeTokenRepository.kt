package `in`.koreatech.koin.domain.repository

class FakeTokenRepository : TokenRepository {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    override suspend fun saveAccessToken(token: String) {
        accessToken = token
    }

    override suspend fun saveRefreshToken(token: String) {
        refreshToken = token
    }

    override suspend fun saveAccessHistoryId(token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getAccessToken(): String? {
        return accessToken
    }

    override suspend fun getRefreshToken(): String? {
        return refreshToken
    }

    override suspend fun getAccessHistoryId(): String? {
        TODO("Not yet implemented")
    }

    override fun getAccessTokenBlocking(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun removeToken() {
        TODO("Not yet implemented")
    }

    override suspend fun saveOwnerAccessToken(token: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getOwnerAccessToken(): String? {
        TODO("Not yet implemented")
    }

    override fun getAccessOwnerTokenBlocking(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun removeOwnerAccessToken() {
        TODO("Not yet implemented")
    }

    override suspend fun removeRefreshToken() {
        TODO("Not yet implemented")
    }
}
