package `in`.koreatech.bus.viewstate

import androidx.compose.runtime.Immutable
import `in`.koreatech.bus.screen.timetable.type.ShuttleBusRouteType

@Immutable
data class ShuttleTimetableOverviewViewState(
    val routeType: ShuttleBusRouteType,
    val name: String,
    val description: String = "",
)

@Immutable
data class ShuttleRegionViewState(
    val name: String,
    val timetableOverviews: List<ShuttleTimetableOverviewViewState>
)

// TODO : 임시 데이터, API 아직 없음