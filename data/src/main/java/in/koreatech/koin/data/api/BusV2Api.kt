package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.bus.v2.BusNoticeResponse
import `in`.koreatech.koin.data.response.bus.v2.BusSearchResultWrapperResponse
import `in`.koreatech.koin.data.response.bus.v2.CityTimetableResponse
import `in`.koreatech.koin.data.response.bus.v2.ExpressTimetableResponse
import `in`.koreatech.koin.data.response.bus.v2.ShuttleCoursesResponse
import `in`.koreatech.koin.data.response.bus.v2.ShuttleTimetableResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BusV2Api {

    @GET("bus/notice")
    suspend fun fetchBusNotice(): BusNoticeResponse

    @GET("bus/timetable/shuttle/{id}")
    suspend fun fetchShuttleTimetable(
        @Path("id") id: String
    ): ShuttleTimetableResponse

    @GET("bus/courses/shuttle")
    suspend fun fetchShuttleCourses(): ShuttleCoursesResponse

    @GET("bus/timetable/v2?bus_type=EXPRESS&region=null")
    suspend fun fetchExpressTimetable(
        @Query("direction") direction: String,
    ): ExpressTimetableResponse

    @GET("bus/timetable/city?direction=종합터미널")
    suspend fun fetchCityTimetable(
        @Query("bus_number") busNumber: Int,
        @Query("direction") direction: String,
    ): CityTimetableResponse

    @GET("bus/route")
    suspend fun fetchBusSearchResult(
        @Query("date") date: String,
        @Query("time") time: String,
        @Query("bus_type") busType: String,
        @Query("depart") departure: String,
        @Query("arrival") arrival: String
    ): BusSearchResultWrapperResponse
}