package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TimetableApi {
    @GET("/semesters")
    suspend fun getSemesters(): List<SemesterResponse>

    @GET("/lectures")
    suspend fun getLectures(
        @Query("semester_date") semesterDate: String
    ): List<LectureResponse>
}