package `in`.koreatech.koin.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnBoardingRepository {
    suspend fun updateShouldShowDiningTooltip(shouldShow: Boolean)
    suspend fun getShouldShowDiningTooltip(): Result<Boolean>
    suspend fun updateShouldShowNotificationOnBoarding(shouldShow: Boolean)
    suspend fun getShouldShowNotificationOnBoarding(): Result<Boolean>
    fun updateShouldShowKeywordTooltip(shouldShow: Boolean): Flow<Unit>
    fun getShouldShowKeywordTooltip(): Flow<Boolean>
}