package `in`.koreatech.koin.domain.usecase.session

import `in`.koreatech.koin.domain.repository.SessionRepository
import javax.inject.Inject

class GetSessionIdUseCase @Inject constructor(
    private val sessionRepository: SessionRepository
) {
    operator fun invoke(
        sessionName: String,
        isLoggedIn: Boolean,
        platform: String = "ANDROID",
        sessionTime: Int = 900,
        shouldExpireOtherSessions: Boolean = false
    ): String {
        return sessionRepository.getSessionId(sessionName, isLoggedIn, platform, sessionTime, shouldExpireOtherSessions)
    }
}
