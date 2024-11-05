package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.OnboardingLocalDataSource
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingLocalDataSource: OnboardingLocalDataSource
) : OnboardingRepository {

    override suspend fun getShouldOnboarding(onboardingType: String): Boolean {
        return onboardingLocalDataSource.getShouldOnboarding(onboardingType)
    }

    override suspend fun updateShouldOnboarding(onboardingType: String, shouldShow: Boolean) {
        onboardingLocalDataSource.updateShouldOnboarding(onboardingType, shouldShow)
    }
}