package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.source.datastore.SessionDataStore
import javax.inject.Inject

class SessionLocalDataSource @Inject constructor(
    private val sessionDataStore: SessionDataStore
) {
    suspend fun getSessionId(
        sessionName: String,
        isLoggedIn: Boolean,
        platform: String,
        sessionTime: Int,
        shouldExpireOtherSessions: Boolean
    ): String {
        return sessionDataStore.getSessionId(sessionName, isLoggedIn, platform, sessionTime, shouldExpireOtherSessions)
    }
}
