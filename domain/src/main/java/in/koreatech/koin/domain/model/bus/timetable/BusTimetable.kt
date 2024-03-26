package `in`.koreatech.koin.domain.model.bus.timetable

sealed class BusTimetable {
    data class ShuttleBusTimetable(
        val routes: List<BusRoute.ShuttleBusRoute>,
        val updatedAt: String
    ) : BusTimetable()

    data class ExpressBusTimetable(
        val routes: BusRoute.ExpressBusRoute,
        val updatedAt: String
    ) : BusTimetable()
}