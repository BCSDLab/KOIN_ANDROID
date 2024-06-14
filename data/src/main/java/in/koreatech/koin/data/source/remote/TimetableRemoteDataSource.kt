package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.TimetableApi
import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import javax.inject.Inject

class TimetableRemoteDataSource @Inject constructor(
    private val timetableApi: TimetableApi,
) {
    suspend fun loadSemesters(): List<SemesterResponse> =
        timetableApi.getSemesters()

    suspend fun loadLectures(semester: String): List<LectureResponse> =
        timetableApi.getLectures(semester)
}