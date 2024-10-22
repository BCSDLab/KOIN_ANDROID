package `in`.koreatech.koin.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    suspend fun updateShouldShowDiningTooltip(shouldShow: Boolean)
    suspend fun getShouldShowDiningTooltip(): Result<Boolean>
    suspend fun updateShouldShowNotificationOnboarding(shouldShow: Boolean)
    suspend fun getShouldShowNotificationOnboarding(): Result<Boolean>
    fun updateShouldShowKeywordTooltip(shouldShow: Boolean): Flow<Unit>
    fun getShouldShowKeywordTooltip(): Flow<Boolean>

    suspend fun getShouldShowTooltip(onboardingType: String): Boolean
    suspend fun updateShouldShowTooltip(onboardingType: String, shouldShow: Boolean)
}