package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.Semester
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures

interface TimetableRepository {
    suspend fun getSemesters(): List<Semester>

    suspend fun getLectures(semesterDate: String): List<Lecture>

    suspend fun getTimetableLectures(): TimetableLectures

    suspend fun putTimetableLectures(
        lectures: TimetableLecturesQuery
    ): TimetableLectures

    suspend fun postTimetableLectures(
        lectures: TimetableLecturesQuery
    ): TimetableLectures

    suspend fun putTimetableFrame(
        id: Int, frame: TimetableFrameQuery
    ): TimetableFrame

    suspend fun postTimetableFrame(
        frame: TimetableFrameCreateQuery
    ): TimetableFrame

    suspend fun deleteTimetableFrame()

    suspend fun getTimetableFrames(
        semester: String
    ): List<TimetableFrame>

    suspend fun deleteTimetableLecture(id: Int)
    suspend fun deleteAllTimetableFrame()
}