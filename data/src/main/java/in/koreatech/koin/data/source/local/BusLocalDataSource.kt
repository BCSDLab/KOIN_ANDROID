package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.response.bus.CityBusRouteResponse
import `in`.koreatech.koin.domain.model.bus.BusDirection
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.course.BusCourse

class BusLocalDataSource {
    fun getCityBusTimetable(): List<CityBusRouteResponse> {
        return listOf(
            CityBusRouteResponse("시간표(터미널)", "6:00(첫) - 22:30(막) (10분간격)"),
            CityBusRouteResponse("시간표(병천)", "6:10(첫) - 22:45(막) (10분간격)"),
            CityBusRouteResponse("소요시간", "약 40분")
        )
    }

    fun getExpressCourses(): List<BusCourse> {
        return listOf(
            BusCourse(
                busType = BusType.Express,
                direction = BusDirection.ToKoreatech,
                region = ""
            ),
            BusCourse(
                busType = BusType.Express,
                direction = BusDirection.FromKoreatech,
                region = ""
            )
        )
    }
}