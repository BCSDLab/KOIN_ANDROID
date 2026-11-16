package `in`.koreatech.koin.feature.land.ui.main

import `in`.koreatech.koin.feature.land.model.LandUiModel

internal enum class LandFilter {
    ALL,
    MONTHLY,
    CHARTER;

    fun matches(land: LandUiModel): Boolean = when (this) {
        ALL -> true
        MONTHLY -> land.isMonthlyAvailable
        CHARTER -> land.isCharterAvailable
    }
}
