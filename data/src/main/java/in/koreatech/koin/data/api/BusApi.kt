package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.bus.*
import retrofit2.http.GET
import retrofit2.http.Query

interface BusApi {
    @GET(URLConstant.BUS.COURSES)
    suspend fun getBusCourses(): List<BusCourseResponse>

    @GET(URLConstant.BUS.TIMETABLE_V2)
    suspend fun getShuttleBusTimetable(
        @Query("bus_type") busType: String, //shuttle, commuting
        @Query("direction") busDirection: String, //to(등교), from(하교)
        @Query("region") region: String //courses의 region
    ) : ShuttleBusTimetableResponse

    @GET(URLConstant.BUS.TIMETABLE_V2 + "?bus_type=express")
    suspend fun getExpressBusTimetable(
        @Query("direction") busDirection: String, //to(등교), from(하교)
        @Query("region") region: String //courses의 region
    ) : ExpressBusTimetableResponse

    @GET(URLConstant.BUS.CITY)
    suspend fun getCityBusTimetable(
        @Query("bus_number") busNumber: Int,
        @Query("direction") direction: String
    ): CityBusTimetableResponse

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