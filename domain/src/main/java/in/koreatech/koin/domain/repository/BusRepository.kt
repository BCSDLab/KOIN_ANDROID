package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.bus.BusNotice
import `in`.koreatech.koin.domain.model.bus.BusSearchResult
import `in`.koreatech.koin.domain.model.bus.CityTimetable
import `in`.koreatech.koin.domain.model.bus.ExpressTimetable
import `in`.koreatech.koin.domain.model.bus.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.ShuttleTimetable
import java.time.LocalDate
import java.time.LocalTime

interface BusRepository {
    suspend fun fetchBusNotice(): Result<BusNotice>

    suspend fun fetchShuttleTimetable(id: String): Result<ShuttleTimetable>

    suspend fun fetchShuttleCourses(): Result<ShuttleCourses>

    suspend fun fetchExpressTimetable(direction: String): Result<ExpressTimetable>

    suspend fun fetchCityTimetable(
        number: Int,
        direction: String,
    ): Result<CityTimetable>

    suspend fun fetchBusSearchResult(
        date: LocalDate,
        time: LocalTime,
        busType: String,
        departure: String,
        arrival: String,
    ): Result<List<BusSearchResult>>

    suspend fun getLastShownNoticeId(): Result<Int>

    suspend fun saveLastShownNoticeId(id: Int): Result<Unit>
}
