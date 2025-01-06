package `in`.koreatech.bus.state

import `in`.koreatech.bus.type.ShuttleBusOperationType
import `in`.koreatech.bus.util.CIRCULATION
import `in`.koreatech.bus.util.WEEKEND
import `in`.koreatech.koin.domain.model.bus.ShuttleCourseRoute

data class ShuttleCourseRouteState(
    val id: String,
    val type: ShuttleBusOperationType,
    val routeName: String,
    val subName: String
)

fun ShuttleCourseRoute.toShuttleCourseRouteState() = ShuttleCourseRouteState(
    id = id,
    type = when(type) {
        CIRCULATION -> ShuttleBusOperationType.CIRCULATION
        WEEKEND -> ShuttleBusOperationType.WEEKEND
        else -> ShuttleBusOperationType.WEEKDAY
    },
    routeName = routeName,
    subName = subName
)
