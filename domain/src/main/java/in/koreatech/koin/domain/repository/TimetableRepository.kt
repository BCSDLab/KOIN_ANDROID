package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.Semester
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {
    fun getSemesters(): Flow<List<Semester>>
    fun getLectures(semesterDate: String): Flow<List<Lecture>>
    fun getTimetableFrames(semester: String): Flow<List<TimetableFrame>>

    suspend fun getTimetableLectures(timetableFrameId: Int): Result<TimetableLectures>
    suspend fun getTimetableLectures(semester: String): Result<TimetableLectures>

    suspend fun putTimetableLectures(lectures: TimetableLecturesQuery): TimetableLectures
    suspend fun putTimetableLectures(key: String, value: TimetableLectures): Result<TimetableLectures>
    suspend fun putTimetableFrame(id: Int, frame: TimetableFrameQuery): TimetableFrame

    suspend fun postTimetableLectures(lectures: TimetableLecturesQuery): TimetableLectures
    suspend fun postTimetableFrame(frame: TimetableFrameCreateQuery): TimetableFrame

    suspend fun deleteTimetableFrame()
    suspend fun deleteTimetableLecture(id: Int)
    suspend fun deleteAllTimetableFrame()
}