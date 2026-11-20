package `in`.koreatech.koin.feature.land.ui.detail

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.feature.land.model.LandDetailUiModel

@Immutable
internal data class LandDetailState(
    val detail: LandDetailUiModel? = null,
    val isLoading: Boolean = true,
    val hasLoadError: Boolean = false,
    val canDialPhone: Boolean = false,
    val isMonthlyAvailable: Boolean = false,
    val isCharterAvailable: Boolean = false
)
