package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.bus.BusCourseResponse
import `in`.koreatech.koin.data.response.bus.BusTimetableResponse
import `in`.koreatech.koin.data.response.bus.NextBusResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface BusApi {
    @GET(URLConstant.BUS.COURSES)
    suspend fun getBusCourses(): List<BusCourseResponse>

    @GET(URLConstant.BUS.TIMETABLE)
    suspend fun getBusTimetable(
        @Query("bus_tupe") busType: String, //shuttle, commuting, express
        @Query("region") region: String //courses의 region
    ) : BusTimetableResponse

    @GET(URLConstant.BUS.BUSES)
    suspend fun getBuses(
        @Query("depart") departure: String,
        @Query("arrival") arrival: String
    ) : NextBusResponse
}