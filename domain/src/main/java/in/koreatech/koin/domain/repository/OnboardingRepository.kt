package `in`.koreatech.koin.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    suspend fun getShouldOnboarding(onboardingType: String): Boolean

    suspend fun updateShouldOnboarding(
        onboardingType: String,
        shouldShow: Boolean,
    )

    fun getShouldOnboardingFlow(onboardingType: String): Flow<Boolean>
}
