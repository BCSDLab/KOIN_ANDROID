package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.v3.LectureResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.SemesterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TimetableApi {
    @GET("/v3/semesters")
    suspend fun getSemestersV3(): List<SemesterResponse>

    @Deprecated("use getLecturesBySemester instead")
    @GET("/lectures")
    suspend fun getLectures(
        @Query("semester_date") semesterDate: String
    ): List<LectureResponse>

    @GET("/v3/lectures")
    suspend fun getLecturesBySemester(
        @Query("year") year: Int,
        @Query("term") term: String
    ): List<LectureResponseV3>
}
