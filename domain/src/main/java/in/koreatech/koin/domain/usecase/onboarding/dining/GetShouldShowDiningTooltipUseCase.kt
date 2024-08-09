package `in`.koreatech.koin.domain.usecase.onboarding.dining

import `in`.koreatech.koin.domain.repository.OnBoardingRepository
import javax.inject.Inject

class GetShouldShowDiningTooltipUseCase @Inject constructor(
    private val onBoardingRepository: OnBoardingRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return onBoardingRepository.getShouldShowDiningTooltip()
    }
}