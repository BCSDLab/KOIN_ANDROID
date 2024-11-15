package `in`.koreatech.koin.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.koreatech.koin.data.request.timetable.LecturesQueryRequest
import `in`.koreatech.koin.data.request.timetable.toCustomLectureQueryRequest
import `in`.koreatech.koin.data.request.timetable.toLectureQueryRequest
import `in`.koreatech.koin.data.request.timetable.toTimetableLecturesQueryRequest
import `in`.koreatech.koin.data.source.datastore.TimetableDataStore
import `in`.koreatech.koin.data.source.remote.TimetableRemoteDataSource
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TimetableRepositoryImpl @Inject constructor(
    private val timetableRemoteDataSource: TimetableRemoteDataSource,
    private val timetableDataStore: TimetableDataStore,
) : TimetableRepository {
    private val gson = Gson()

    override fun getSemesters(): Flow<List<String>> = flow {
        emit(timetableRemoteDataSource.getSemesters().map { it.toSemester().semester })
    }

    override fun getSemesterCheck(): Flow<List<String>> = flow {
        emit(timetableRemoteDataSource.getSemesterCheck().toSemesterCheck().semesters)
    }

    override fun getLectures(semesterDate: String): Flow<List<Lecture>> = flow {
        emit(timetableRemoteDataSource.getLectures(semesterDate).map { it.toLecture() })
    }

    override fun getTimetableFrames(semester: String): Flow<List<TimetableFrame>> = flow {
        emit(timetableRemoteDataSource.getTimetableFrames(semester).map { it.toTimetableFrameResponse() })
    }

    override suspend fun getTimetableLectures(timetableFrameId: Int): Result<TimetableLectures> = runCatching {
        timetableRemoteDataSource.getTimetableLectures(timetableFrameId).toTimetableLectures()
    }

    override suspend fun getTimetableLectures(semester: String): Result<TimetableLectures> = runCatching{
        val timetableLecturesString = timetableDataStore.getString(semester).firstOrNull().orEmpty()
        val timetableLecturesType = object : TypeToken<TimetableLectures>() {}.type
        try {
            gson.fromJson(timetableLecturesString, timetableLecturesType)
        } catch (e: NullPointerException) {
            TimetableLectures(0, emptyList(), 0, 0)
        }
    }

    override suspend fun putTimetableLectures(lectures: TimetableLecturesQuery): TimetableLectures =
        timetableRemoteDataSource.putTimetableLectures(lectures.toTimetableLecturesQueryRequest()).toTimetableLectures()

    override suspend fun putTimetableLectures(key: String, value: TimetableLectures): Result<TimetableLectures> = runCatching {
        timetableDataStore.putString(key, gson.toJson(value))
        return getTimetableLectures(semester = key).onSuccess {
            Result.success(it)
        }.onFailure {
            Result.failure<TimetableLectures>(it)
        }
    }

    override suspend fun putTimetableFrame(id: Int, frame: TimetableFrameQuery): TimetableFrame {
        TODO("Not yet implemented")
    }

    override suspend fun postTimetableLectures(frameId: Int, lectures: List<Lecture>): Result<TimetableLectures> = runCatching {
        timetableRemoteDataSource.postTimetableLectures(LecturesQueryRequest(
            timetableFrameId = frameId,
            timetableLecture = lectures.map { it.toLectureQueryRequest() }
        )).toTimetableLectures()
    }

    override suspend fun postTimetableCustomLectures(frameId: Int, lectures: List<Lecture>): Result<TimetableLectures> = runCatching {
        timetableRemoteDataSource.postTimetableLectures(LecturesQueryRequest(
            timetableFrameId = frameId,
            timetableLecture = lectures.map { it.toCustomLectureQueryRequest() }
        )).toTimetableLectures()
    }


    override suspend fun postTimetableFrame(frame: TimetableFrameCreateQuery): TimetableFrame {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTimetableFrame() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTimetableLecture(id: Int): Result<Unit> = runCatching {
        timetableRemoteDataSource.deleteTimetableLecture(id)
    }

    override suspend fun deleteTimetableFrameLecture(frameId: Int, lectureId: Int): Result<Unit> = runCatching {
        timetableRemoteDataSource.deleteTimetableFrameLecture(frameId, lectureId)
    }

    override suspend fun deleteTimetableLectures(lectureIds: List<Int>): Result<Unit> = runCatching {
        timetableRemoteDataSource.deleteTimetableLectures(lectureIds)
    }

    override suspend fun deleteAllTimetableFrame() {
        TODO("Not yet implemented")
    }
}