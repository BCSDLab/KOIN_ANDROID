package `in`.koreatech.koin.domain.repository

interface OnBoardingRepository {
    suspend fun updateShouldShowDiningTooltip(shouldShow: Boolean)
    suspend fun getShouldShowDiningTooltip(): Result<Boolean>
    suspend fun updateShouldShowNotificationOnBoarding(shouldShow: Boolean)
    suspend fun getShouldShowNotificationOnBoarding(): Result<Boolean>
}