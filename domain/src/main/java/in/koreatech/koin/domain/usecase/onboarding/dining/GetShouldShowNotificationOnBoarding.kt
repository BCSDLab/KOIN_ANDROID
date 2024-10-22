package `in`.koreatech.koin.domain.usecase.onboarding.dining

import `in`.koreatech.koin.domain.repository.OnboardingRepository
import javax.inject.Inject

class GetShouldShowNotificationOnBoarding @Inject constructor(
    private val onBoardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return onBoardingRepository.getShouldShowNotificationOnboarding()
    }
}