package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.TimetableApi
import `in`.koreatech.koin.data.api.auth.TimetableAuthApi
import `in`.koreatech.koin.data.request.timetable.TimetablesRequest
import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import `in`.koreatech.koin.data.response.timetable.TimetablesResponse
import javax.inject.Inject

class TimetableRemoteDataSource @Inject constructor(
    private val timetableApi: TimetableApi,
    private val timetableAuthApi: TimetableAuthApi,
) {
    suspend fun loadTimetables(semester: String): TimetablesResponse =
        timetableAuthApi.getTimetables(semester)

    suspend fun loadSemesters(): List<SemesterResponse> =
        timetableApi.getSemesters()

    suspend fun loadLectures(semester: String): List<LectureResponse> =
        timetableApi.getLectures(semester)

    suspend fun updateTimetables(timetablesRequest: TimetablesRequest) {
        timetableAuthApi.postTimetables(timetablesRequest)
    }

    suspend fun deleteTimetables(id: Int) {
        timetableAuthApi.deleteTimetables(id)
    }
}