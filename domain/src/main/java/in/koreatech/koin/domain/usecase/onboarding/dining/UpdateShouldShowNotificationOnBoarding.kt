package `in`.koreatech.koin.domain.usecase.onboarding.dining

import `in`.koreatech.koin.domain.repository.OnBoardingRepository
import javax.inject.Inject

class UpdateShouldShowNotificationOnBoarding @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) {
    suspend operator fun invoke(shouldShow: Boolean) {
        onBoardingRepository.updateShouldShowNotificationOnBoarding(shouldShow)
    }
}