package `in`.koreatech.koin.domain.model.bus

data class ShuttleTimetableRouteInfo(
    val name: String,
    val detail: String,
    val arrivalTimes: List<String>
)
