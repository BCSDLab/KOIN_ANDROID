package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.bus.*
import retrofit2.http.GET
import retrofit2.http.Query

interface BusApi {
    @GET(URLConstant.BUS.COURSES)
    suspend fun getBusCourses(): List<BusCourseResponse>

    @GET(URLConstant.BUS.TIMETABLE)
    suspend fun getBusTimetable(
        @Query("bus_type") busType: String, //shuttle, commuting, express
        @Query("direction") busDirection: String, //to(등교), from(하교)
        @Query("region") region: String //courses의 region
    ) : List<BusTimetableResponse>

    @GET(URLConstant.BUS.TIMETABLE + "?bus_type=express")
    suspend fun getExpressBusTimetable(
        @Query("direction") busDirection: String, //to(등교), from(하교)
        @Query("region") region: String //courses의 region
    ) : List<ExpressBusRouteResponse>

    @GET(URLConstant.BUS.BUS)
    suspend fun getBus(
        @Query("bus_type") busType: String, //shuttle, commuting, express, city
        @Query("depart") departure: String, //koreatech, station, terminal
        @Query("arrival") arrival: String //koreatech, station, terminal
    ) : BusResponse

    @GET(URLConstant.BUS.SEARCH)
    suspend fun searchBus(
        @Query("date") date: String, // yyyy-MM-dd
        @Query("time") time: String, // HH:mm
        @Query("depart") departure: String, //koreatech, station, terminal
        @Query("arrival") arrival: String //koreatech, station, terminal
    ) : List<BusSearchResponse>
}