package `in`.koreatech.koin.domain.repository

interface OnboardingRepository {
    suspend fun updateShouldShowNotificationOnboarding(shouldShow: Boolean)
    suspend fun getShouldShowNotificationOnboarding(): Result<Boolean>

    suspend fun getShouldShowTooltip(onboardingType: String): Boolean
    suspend fun updateShouldShowTooltip(onboardingType: String, shouldShow: Boolean)
}