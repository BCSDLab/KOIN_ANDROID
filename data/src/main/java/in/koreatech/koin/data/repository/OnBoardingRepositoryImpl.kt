package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.local.OnBoardingLocalDataSource
import `in`.koreatech.koin.domain.repository.OnBoardingRepository
import javax.inject.Inject

class OnBoardingRepositoryImpl @Inject constructor(
    private val onBoardingLocalDataSource: OnBoardingLocalDataSource
) : OnBoardingRepository {
    override suspend fun updateShouldShowDiningTooltip(shouldShow: Boolean) {
        onBoardingLocalDataSource.updateShouldShowDiningTooltip(shouldShow)
    }

    override suspend fun getShouldShowDiningTooltip(): Result<Boolean> {
        return onBoardingLocalDataSource.getShouldShowDiningTooltip()
    }
}