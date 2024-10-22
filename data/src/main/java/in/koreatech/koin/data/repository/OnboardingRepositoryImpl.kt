package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.OnboardingLocalDataSource
import `in`.koreatech.koin.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val onboardingLocalDataSource: OnboardingLocalDataSource
) : OnboardingRepository {

    override suspend fun getShouldShowTooltip(onboardingType: String): Boolean {
        return onboardingLocalDataSource.getShouldShowTooltip(onboardingType)
    }

    override suspend fun updateShouldShowTooltip(onboardingType: String, shouldShow: Boolean) {
        onboardingLocalDataSource.updateShouldShowTooltip(onboardingType, shouldShow)
    }
}