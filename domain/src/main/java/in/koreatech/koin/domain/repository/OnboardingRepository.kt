package `in`.koreatech.koin.domain.repository

interface OnboardingRepository {
    suspend fun getShouldOnboarding(onboardingType: String): Boolean
    suspend fun updateShouldOnboarding(onboardingType: String, shouldShow: Boolean)
}