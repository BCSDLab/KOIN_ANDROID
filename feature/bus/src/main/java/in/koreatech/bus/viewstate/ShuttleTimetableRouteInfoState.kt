package `in`.koreatech.bus.viewstate

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetableRouteInfo

@Immutable
data class ShuttleTimetableRouteInfoState(
    val name: String,
    val arrivalTimes: List<String>,
)

fun ShuttleTimetableRouteInfo.toShuttleTimetableRouteInfoState() = ShuttleTimetableRouteInfoState(
    name = name,
    arrivalTimes = arrivalTimes
)