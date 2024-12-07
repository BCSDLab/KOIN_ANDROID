package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.bus.v2.BusNotice
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleCourses
import `in`.koreatech.koin.domain.model.bus.v2.ShuttleTimetable

interface BusV2Repository {
    suspend fun fetchBusNotice(): Result<BusNotice>
    suspend fun fetchShuttleTimetable(id: String): Result<ShuttleTimetable>
    suspend fun fetchShuttleCourses(): Result<ShuttleCourses>
}