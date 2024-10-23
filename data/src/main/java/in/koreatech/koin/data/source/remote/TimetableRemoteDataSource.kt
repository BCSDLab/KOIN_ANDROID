package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.TimetableApi
import `in`.koreatech.koin.data.api.auth.TimetableAuthApi
import `in`.koreatech.koin.data.request.timetable.TimetableFrameCreateQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableLecturesQueryRequest
import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import `in`.koreatech.koin.data.response.timetable.TimetableFrameResponse
import `in`.koreatech.koin.data.response.timetable.TimetableLecturesResponse
import javax.inject.Inject

class TimetableRemoteDataSource @Inject constructor(
    private val timetableApi: TimetableApi,
    private val timetableAuthApi: TimetableAuthApi
) {
    suspend fun getSemesters(): List<SemesterResponse> = timetableApi.getSemesters()

    suspend fun getLectures(semesterDate: String): List<LectureResponse> =
        timetableApi.getLectures(semesterDate)

    suspend fun getTimetableLectures(): TimetableLecturesResponse =
        timetableAuthApi.getTimetableLectures()

    suspend fun putTimetableLectures(
        lectures: TimetableLecturesQueryRequest
    ): TimetableLecturesResponse = timetableAuthApi.putTimetableLectures(lectures)

    suspend fun postTimetableLectures(
        lectures: TimetableLecturesQueryRequest
    ): TimetableLecturesResponse = timetableAuthApi.postTimetableLectures(lectures)

    suspend fun putTimetableFrame(
        id: Int, frame: TimetableFrameQueryRequest
    ): TimetableFrameResponse = timetableAuthApi.putTimetableFrame(id, frame)

    suspend fun postTimetableFrame(
        frame: TimetableFrameCreateQueryRequest
    ): TimetableFrameResponse = timetableAuthApi.postTimetableFrame(frame)

    suspend fun deleteTimetableFrame() = timetableAuthApi.deleteTimetableFrame()

    suspend fun getTimetableFrames(
        semester: String
    ): List<TimetableFrameResponse> = timetableAuthApi.getTimetableFrames(semester)

    suspend fun deleteTimetableLecture(
        id: Int
    ) = timetableAuthApi.deleteTimetableLecture(id)

    suspend fun deleteAllTimetableFrame() = timetableAuthApi.deleteAllTimetableFrame()
}