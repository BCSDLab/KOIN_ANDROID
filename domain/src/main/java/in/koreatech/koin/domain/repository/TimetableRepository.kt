package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.timetable.SemesterV3
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {
    fun getSemestersV3(): Flow<List<SemesterV3>>

    fun getUserSemesters(): Flow<List<SemesterV3>>

    fun getLectures(semesterDate: String): Flow<List<Lecture>>

    fun getTimetableFrames(semester: String): Flow<List<TimetableFrame>>

    fun getAllFrames(): Flow<Map<String, List<TimetableFrame>>>

    suspend fun getTimetableLectures(timetableFrameId: Int): Result<TimetableLectures>

    suspend fun getTimetableLectures(semester: String): Result<TimetableLectures>

    suspend fun putTimetableLectures(lectures: TimetableLecturesQuery): Result<TimetableLectures>

    suspend fun putTimetableLectures(
        key: String,
        value: TimetableLectures,
    ): Result<TimetableLectures>

    suspend fun putTimetableFrame(
        id: Int,
        frame: TimetableFrameQuery,
    ): Result<TimetableFrame>

    suspend fun postTimetableLectures(
        frameId: Int,
        lectures: List<Lecture>,
    ): Result<TimetableLectures>

    suspend fun postTimetableCustomLectures(
        frameId: Int,
        lectures: List<Lecture>,
    ): Result<TimetableLectures>

    suspend fun postTimetableBasicLectures(
        frameId: Int,
        lectures: List<TimetableLecture>,
    ): Result<TimetableLectures>

    suspend fun postTimetableFrame(frame: TimetableFrameCreateQuery): Result<TimetableFrame>

    suspend fun postRollbackFrame(frameId: Int): Result<TimetableLectures>

    suspend fun deleteTimetableFrame(frameId: Int): Result<Unit>

    suspend fun deleteTimetableLecture(id: Int): Result<Unit>

    suspend fun deleteTimetableLectures(lectureIds: List<Int>): Result<Unit>

    suspend fun deleteTimetableFrameLecture(
        frameId: Int,
        lectureId: Int,
    ): Result<Unit>

    suspend fun deleteAllTimetableFrame(semester: String): Result<Unit>
}
