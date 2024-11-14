package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.TimetableApi
import `in`.koreatech.koin.data.api.auth.TimetableAuthApi
import `in`.koreatech.koin.data.request.timetable.LectureCustomQueryRequest
import `in`.koreatech.koin.data.request.timetable.LecturesCustomQueryRequest
import `in`.koreatech.koin.data.request.timetable.LecturesQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameCreateQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableLecturesQueryRequest
import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.SemesterCheckResponse
import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import `in`.koreatech.koin.data.response.timetable.TimetableFrameResponse
import `in`.koreatech.koin.data.response.timetable.TimetableLecturesResponse
import javax.inject.Inject

class TimetableRemoteDataSource @Inject constructor(
    private val timetableApi: TimetableApi,
    private val timetableAuthApi: TimetableAuthApi
) {
    suspend fun getSemesters(): List<SemesterResponse> = timetableApi.getSemesters()

    suspend fun getSemesterCheck(): SemesterCheckResponse = timetableAuthApi.getSemestersCheck()

    suspend fun getLectures(semesterDate: String): List<LectureResponse> =
        timetableApi.getLectures(semesterDate)

    suspend fun getTimetableLectures(timetableFrameId: Int): TimetableLecturesResponse =
        timetableAuthApi.getTimetableLectures(timetableFrameId)

    suspend fun getTimetableFrames(
        semester: String
    ): List<TimetableFrameResponse> = timetableAuthApi.getTimetableFrames(semester)

    suspend fun putTimetableLectures(
        lectures: TimetableLecturesQueryRequest
    ): TimetableLecturesResponse = timetableAuthApi.putTimetableLectures(lectures)

    suspend fun putTimetableFrame(
        id: Int, frame: TimetableFrameQueryRequest
    ): TimetableFrameResponse = timetableAuthApi.putTimetableFrame(id, frame)

    suspend fun postTimetableLectures(
        lectures: LecturesQueryRequest
    ): TimetableLecturesResponse = timetableAuthApi.postTimetableLectures(lectures)


    suspend fun postTimetableLectures(
        lectures: LecturesCustomQueryRequest
    ): TimetableLecturesResponse = timetableAuthApi.postTimetableLectures(lectures)

    suspend fun postTimetableFrame(
        frame: TimetableFrameCreateQueryRequest
    ): TimetableFrameResponse = timetableAuthApi.postTimetableFrame(frame)

    suspend fun deleteTimetableFrame() = timetableAuthApi.deleteTimetableFrame()


    suspend fun deleteTimetableLecture(
        id: Int
    ) = timetableAuthApi.deleteTimetableLecture(id)

    suspend fun deleteTimetableFrameLecture(
        frameId: Int,
        lectureId: Int
    ) = timetableAuthApi.deleteTimetableFrameLecture(frameId, lectureId)

    suspend fun deleteTimetableLectures(
        lectureIds: List<Int>
    ) = timetableAuthApi.deleteTimetableLectures(lectureIds)

    suspend fun deleteAllTimetableFrame() = timetableAuthApi.deleteAllTimetableFrame()
}