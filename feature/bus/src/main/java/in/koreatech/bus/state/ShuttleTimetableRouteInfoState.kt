package `in`.koreatech.bus.state

import androidx.compose.runtime.Immutable
import `in`.koreatech.koin.domain.model.bus.ShuttleTimetableRouteInfo

@Immutable
data class ShuttleTimetableRouteInfoState(
    val name: String,
    val detail: String,
    val arrivalTimes: List<String>
)

fun ShuttleTimetableRouteInfo.toShuttleTimetableRouteInfoState() =
    ShuttleTimetableRouteInfoState(
        name = name,
        detail = detail,
        arrivalTimes = arrivalTimes
    )
