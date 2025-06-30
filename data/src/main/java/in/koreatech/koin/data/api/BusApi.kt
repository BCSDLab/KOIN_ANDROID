package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.bus.BusNoticeResponse
import `in`.koreatech.koin.data.response.bus.BusSearchResultWrapperResponse
import `in`.koreatech.koin.data.response.bus.CityTimetableResponse
import `in`.koreatech.koin.data.response.bus.ExpressTimetableResponse
import `in`.koreatech.koin.data.response.bus.ShuttleCoursesResponse
import `in`.koreatech.koin.data.response.bus.ShuttleTimetableResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BusApi {
    @GET(URLConstant.BUS.NOTICE)
    suspend fun fetchBusNotice(): BusNoticeResponse

    @GET(URLConstant.BUS.TIMETABLE.SHUTTLEBYID)
    suspend fun fetchShuttleTimetable(
        @Path("id") id: String
    ): ShuttleTimetableResponse

    @GET(URLConstant.BUS.SHUTTLE)
    suspend fun fetchShuttleCourses(): ShuttleCoursesResponse

    @GET(URLConstant.BUS.TIMETABLE.EXPRESS)
    suspend fun fetchExpressTimetable(
        @Query("direction") direction: String
    ): ExpressTimetableResponse

    @GET(URLConstant.BUS.TIMETABLE.CITY)
    suspend fun fetchCityTimetable(
        @Query("bus_number") busNumber: Int,
        @Query("direction") direction: String
    ): CityTimetableResponse

    @GET(URLConstant.BUS.ROUTE)
    suspend fun fetchBusSearchResult(
        @Query("date") date: String,
        @Query("time") time: String,
        @Query("bus_type") busType: String,
        @Query("depart") departure: String,
        @Query("arrival") arrival: String
    ): BusSearchResultWrapperResponse
}
