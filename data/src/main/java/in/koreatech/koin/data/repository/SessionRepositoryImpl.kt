package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.SessionLocalDataSource
import `in`.koreatech.koin.domain.repository.SessionRepository
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

class SessionRepositoryImpl @Inject constructor(
    private val sessionLocalDataSource: SessionLocalDataSource
) : SessionRepository {
    override fun getSessionId(sessionName: String, isLoggedIn: Boolean, platform: String, sessionTime: Int, shouldExpireOtherSessions: Boolean): String {
        return runBlocking {
            sessionLocalDataSource.getSessionId(sessionName, isLoggedIn, platform, sessionTime, shouldExpireOtherSessions)
        }
    }
}
