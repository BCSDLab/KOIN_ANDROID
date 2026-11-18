package `in`.koreatech.koin.feature.land.ui.search

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.land.model.LandUiModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class LandSearchState(
    val query: String = "",
    val allLands: ImmutableList<LandUiModel> = persistentListOf(),
    val isLoading: Boolean = true,
    val hasLoadError: Boolean = false
)
