package `in`.koreatech.koin.domain.usecase.onboarding.dining

import `in`.koreatech.koin.domain.repository.OnboardingRepository
import javax.inject.Inject

class UpdateShouldShowNotificationOnBoarding @Inject constructor(
    private val onBoardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(shouldShow: Boolean) {
        onBoardingRepository.updateShouldShowNotificationOnboarding(shouldShow)
    }
}