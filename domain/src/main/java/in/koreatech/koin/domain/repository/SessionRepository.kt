package `in`.koreatech.koin.domain.repository

interface SessionRepository {
    fun getSessionId(
        sessionName: String,
        isLoggedIn: Boolean,
        platform: String,
        sessionTime: Int,
        shouldExpireOtherSessions: Boolean
    ): String
}
