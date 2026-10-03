package `in`.koreatech.koin.data.source.remote

import `in`.koreatech.koin.data.api.TimetableApi
import `in`.koreatech.koin.data.api.auth.TimetableAuthApi
import `in`.koreatech.koin.data.request.timetable.v3.TimetableCustomLectureRequest
import `in`.koreatech.koin.data.request.timetable.v3.TimetableFrameCreateRequestV3
import `in`.koreatech.koin.data.request.timetable.v3.TimetableFrameUpdateRequestV3
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureCreateRequest
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureRequest
import `in`.koreatech.koin.data.response.timetable.v3.LectureResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.SemesterResponse
import `in`.koreatech.koin.data.response.timetable.v3.TimetableFrameResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.TimetableFramesResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.TimetableLecturesResponseV3
import `in`.koreatech.koin.data.response.timetable.v3.UserSemestersResponse
import javax.inject.Inject

class TimetableRemoteDataSource @Inject constructor(
    private val timetableApi: TimetableApi,
    private val timetableAuthApi: TimetableAuthApi
) {
    suspend fun getSemestersV3(): List<SemesterResponse> = timetableApi.getSemestersV3()

    suspend fun getUserSemesters(): UserSemestersResponse = timetableAuthApi.getUserSemesters()

    suspend fun getLectures(
        year: Int,
        term: String
    ): List<LectureResponseV3> = timetableApi.getLecturesBySemester(year, term)

    suspend fun getTimetableLectures(timetableFrameId: Int): TimetableLecturesResponseV3 = timetableAuthApi.getLecturesByFrameId(timetableFrameId)

    suspend fun getTimetableFrames(
        year: Int,
        term: String
    ): List<TimetableFrameResponseV3> = timetableAuthApi.getFramesBySemester(year, term)

    suspend fun getAllFrames(): List<TimetableFramesResponseV3> = timetableAuthApi.getAllFramesV3()

    suspend fun putTimetableRegularLecture(lecture: TimetableRegularLectureRequest): TimetableLecturesResponseV3 =
        timetableAuthApi.editTimetableRegularLecture(lecture)

    suspend fun putTimetableCustomLecture(lecture: TimetableCustomLectureRequest): TimetableLecturesResponseV3 =
        timetableAuthApi.editTimetableCustomLecture(lecture)

    suspend fun putTimetableFrame(
        id: Int,
        frame: TimetableFrameUpdateRequestV3
    ): List<TimetableFrameResponseV3> = timetableAuthApi.editFrame(id, frame)

    suspend fun postTimetableRegularLecture(lecture: TimetableRegularLectureCreateRequest): TimetableLecturesResponseV3 =
        timetableAuthApi.addRegularLectureOnTimetable(lecture)

    suspend fun postTimetableCustomLecture(lecture: TimetableCustomLectureRequest): TimetableLecturesResponseV3 =
        timetableAuthApi.addCustomLectureOnTimetable(lecture)

    suspend fun postTimetableFrame(frame: TimetableFrameCreateRequestV3): List<TimetableFrameResponseV3> = timetableAuthApi.createFrame(frame)

    suspend fun postRollbackFrame(frameId: Int): TimetableLecturesResponseV3 = timetableAuthApi.restoreFrameByFrameId(frameId)

    suspend fun deleteTimetableFrame(frameId: Int) = timetableAuthApi.deleteTimetableFrame(frameId)

    suspend fun deleteTimetableLecture(id: Int) = timetableAuthApi.deleteTimetableLecture(id)

    suspend fun deleteTimetableFrameLecture(
        frameId: Int,
        lectureId: Int
    ) = timetableAuthApi.deleteTimetableFrameLecture(frameId, lectureId)

    suspend fun deleteTimetableLectures(lectureIds: List<Int>) = timetableAuthApi.deleteTimetableLectures(lectureIds)

    suspend fun deleteAllTimetableFrame(
        year: Int,
        term: String
    ) = timetableAuthApi.deleteFramesBySemester(year, term)
}
