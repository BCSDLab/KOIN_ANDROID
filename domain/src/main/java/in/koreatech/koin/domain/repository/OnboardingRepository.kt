package `in`.koreatech.koin.domain.repository

interface OnboardingRepository {
    suspend fun getShouldShowTooltip(onboardingType: String): Boolean
    suspend fun updateShouldShowTooltip(onboardingType: String, shouldShow: Boolean)
}