package `in`.koreatech.koin.data.api.auth

import `in`.koreatech.koin.data.constant.URLConstant.TIMETABLE
import `in`.koreatech.koin.data.constant.URLConstant.TIMETABLES
import `in`.koreatech.koin.data.request.timetable.TimetablesRequest
import `in`.koreatech.koin.data.response.timetable.TimetablesResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TimetableAuthApi {
    @GET(TIMETABLES)
    suspend fun getTimetables(
        @Query("semester") semester: String,
    ): TimetablesResponse

    @POST(TIMETABLES)
    suspend fun postTimetables(
        @Body timetables: TimetablesRequest
    )

    @DELETE(TIMETABLE)
    suspend fun deleteTimetables(
        @Query("id") id: Int
    )
}