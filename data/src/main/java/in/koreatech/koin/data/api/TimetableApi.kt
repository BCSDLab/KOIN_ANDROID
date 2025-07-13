package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant
import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.v3.LectureResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.SemesterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TimetableApi {
    @GET(URLConstant.TIMETABLE.V3.SEMESTERS.SEMESTERS)
    suspend fun getSemestersV3(): List<SemesterResponse>

    @Deprecated("use getLecturesBySemester instead")
    @GET(URLConstant.LECTURES.LECTURES)
    suspend fun getLectures(
        @Query("semester_date") semesterDate: String
    ): List<LectureResponse>

    @GET(URLConstant.LECTURES.V3_LECTURES)
    suspend fun getLecturesBySemester(
        @Query("year") year: Int,
        @Query("term") term: String
    ): List<LectureResponseV3>
}
