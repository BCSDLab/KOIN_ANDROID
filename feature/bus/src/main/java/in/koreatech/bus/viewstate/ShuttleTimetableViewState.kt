package `in`.koreatech.bus.viewstate

import androidx.compose.runtime.Immutable

@Immutable
data class ShuttleTimetableViewState(
    val nodes: List<ShuttleNodeViewState>,
    val routes: List<ShuttleRouteViewState>
)

@Immutable
data class ShuttleNodeViewState(
    val title: String,
    val description: String,
)

@Immutable
data class ShuttleRouteViewState(
    val name: String,
    val arrivalTimes: List<String>
)