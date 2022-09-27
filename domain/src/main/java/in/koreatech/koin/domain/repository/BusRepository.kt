package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.bus.BusCourse
import `in`.koreatech.koin.domain.model.bus.BusNode
import `in`.koreatech.koin.domain.model.bus.ExpressArrivalInfo
import `in`.koreatech.koin.domain.model.bus.ShuttleBusRoute

interface BusRepository{
    suspend fun getBusCourses(): List<BusCourse>
    suspend fun getShuttleBusTimetable(region: String): Pair<ShuttleBusRoute, ShuttleBusRoute>
    suspend fun getCommutingBusTimetable(region: String): Pair<ShuttleBusRoute, ShuttleBusRoute>
    suspend fun getExpressBusTimetable(region: String): Pair<List<ExpressArrivalInfo>, List<ExpressArrivalInfo>>
}