package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant.LECTURE
import `in`.koreatech.koin.data.constant.URLConstant.SEMESTERS
import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TimetableApi {
    @GET(SEMESTERS)
    suspend fun getSemesters(): List<SemesterResponse>

    @GET(LECTURE)
    suspend fun getLectures(
        @Query("semester_date") semester: String
    ): List<LectureResponse>
}