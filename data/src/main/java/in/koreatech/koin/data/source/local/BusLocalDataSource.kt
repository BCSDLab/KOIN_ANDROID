package `in`.koreatech.koin.data.source.local

import `in`.koreatech.koin.data.R
import `in`.koreatech.koin.data.response.bus.CityBusRouteResponse
import `in`.koreatech.koin.domain.model.bus.BusDirection
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.course.BusCourse
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BusLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getCityBusTimetable(): List<CityBusRouteResponse> {
        return listOf(
            CityBusRouteResponse(context.getString(R.string.bus_citybus_timetable_terminal_node), context.getString(R.string.bus_citybus_timetable_terminal_time)),
            CityBusRouteResponse(context.getString(R.string.bus_citybus_timetable_byeongcheon_node), context.getString(R.string.bus_citybus_timetable_byeongcheon_time)),
            CityBusRouteResponse(context.getString(R.string.bus_citybus_timetable_require_time), context.getString(R.string.bus_citybus_timetable_require_time_value))
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