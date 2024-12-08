package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.bus.v2.BusNotice
import `in`.koreatech.koin.domain.model.bus.v2.BusSearchResult
import `in`.koreatech.koin.domain.model.bus.v2.CityTimetable
import `in`.koreatech.koin.domain.model.bus.v2.ExpressTimetable
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetable
import java.time.LocalDate
import java.time.LocalTime

interface BusV2Repository {
    suspend fun fetchBusNotice(): Result<BusNotice>
    suspend fun fetchShuttleTimetable(id: String): Result<ShuttleTimetable>
    suspend fun fetchShuttleCourses(): Result<ShuttleCourses>
    suspend fun fetchExpressTimetable(direction: String): Result<ExpressTimetable>
    suspend fun fetchCityTimetable(number: Int, direction: String): Result<CityTimetable>
    suspend fun fetchBusSearchResult(
        date: LocalDate,
        time: LocalTime,
        busType: String,
        departure: String,
        arrival: String
    ): Result<List<BusSearchResult>>
}