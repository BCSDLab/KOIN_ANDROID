package `in`.koreatech.koin.feature.land.ui.main

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.land.model.LandUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class LandState(
    val isLoading: Boolean = false,
    val lands: ImmutableList<LandUiModel> = persistentListOf(),
    val filter: LandFilter = LandFilter.ALL,
    val hasLoadError: Boolean = false,
    val selectedLandId: Int? = null
)
