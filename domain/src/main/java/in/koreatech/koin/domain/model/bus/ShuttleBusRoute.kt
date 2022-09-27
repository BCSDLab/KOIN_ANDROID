package `in`.koreatech.koin.domain.model.bus

data class ShuttleBusRoute(
    val routeName: String,
    val runningDays: List<BusRunningDay>,
    val arrivalInfo: ShuttleArrivalInfo
)
