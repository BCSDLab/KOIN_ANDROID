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

    data class CityBusTimetable(
        val busInfos: BusNodeInfo.CityBusNodeInfo,
        val departTimes: List<String>,  // 평일 or 주말 or 공휴일 or 임시 출발시간 리스트
        val updatedAt: String
    ): BusTimetable()
}