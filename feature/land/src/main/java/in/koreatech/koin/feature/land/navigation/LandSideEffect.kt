package `in`.koreatech.koin.feature.land.navigation

import `in`.koreatech.koin.feature.land.model.LandUiModel

internal sealed interface LandSideEffect {
    data object NavigateBack : LandSideEffect
    data class NavigateToDetail(val land: LandUiModel) : LandSideEffect
}
