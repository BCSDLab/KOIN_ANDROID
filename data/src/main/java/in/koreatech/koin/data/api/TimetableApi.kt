package `in`.koreatech.koin.data.api

import `in`.koreatech.koin.data.constant.URLConstant.SEMESTERS
import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import retrofit2.http.GET

interface TimetableApi {
    @GET(SEMESTERS)
    suspend fun getSemesters(): List<SemesterResponse>
}