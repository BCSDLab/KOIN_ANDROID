package `in`.koreatech.koin.domain.usecase.onboarding.dining

import `in`.koreatech.koin.domain.repository.OnboardingRepository
import javax.inject.Inject

class GetShouldShowDiningTooltipUseCase @Inject constructor(
    private val onBoardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return onBoardingRepository.getShouldShowDiningTooltip()
    }
}