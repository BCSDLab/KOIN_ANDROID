package `in`.koreatech.koin.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.koreatech.koin.data.request.timetable.LectureQueryRequest
import `in`.koreatech.koin.data.request.timetable.LecturesQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameCreateQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableFrameQueryRequest
import `in`.koreatech.koin.data.request.timetable.TimetableLectureClassInfoRequest
import `in`.koreatech.koin.data.request.timetable.toCustomLectureQueryRequest
import `in`.koreatech.koin.data.request.timetable.toLectureQueryRequest
import `in`.koreatech.koin.data.request.timetable.toTimetableLecturesQueryRequest
import `in`.koreatech.koin.data.response.timetable.v3.toSemester
import `in`.koreatech.koin.data.response.timetable.v3.toSemesters
import `in`.koreatech.koin.data.source.datastore.TimetableDataStore
import `in`.koreatech.koin.data.source.remote.TimetableRemoteDataSource
import `in`.koreatech.koin.domain.model.timetable.Semester
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLecture
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import `in`.koreatech.koin.domain.util.suspendRunCatching
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class TimetableRepositoryImpl @Inject constructor(
    private val timetableRemoteDataSource: TimetableRemoteDataSource,
    private val timetableDataStore: TimetableDataStore
) : TimetableRepository {
    private val gson = Gson()

    override fun getSemesters(): Flow<List<Semester>> =
        flow {
            emit(timetableRemoteDataSource.getSemestersV3().map { it.toSemester() })
        }

    override fun getUserSemesters(): Flow<List<Semester>> =
        flow {
            emit(timetableRemoteDataSource.getUserSemesters().toSemesters())
        }

    override fun getLectures(semesterDate: String): Flow<List<Lecture>> =
        flow {
            emit(timetableRemoteDataSource.getLectures(semesterDate).map { it.toLecture() })
        }

    override fun getTimetableFrames(semester: String): Flow<List<TimetableFrame>> =
        flow {
            emit(timetableRemoteDataSource.getTimetableFrames(semester).map { it.toTimetableFrame() })
        }

    override fun getAllFrames(): Flow<Map<String, List<TimetableFrame>>> =
        flow {
            emit(timetableRemoteDataSource.getAllFrames().mapValues { it.value.map { it.toTimetableFrame() } })
        }

    override suspend fun getTimetableLectures(timetableFrameId: Int): Result<TimetableLectures> =
        suspendRunCatching {
            timetableRemoteDataSource.getTimetableLectures(timetableFrameId).toTimetableLectures()
        }

    override suspend fun getTimetableLectures(semester: String): Result<TimetableLectures> =
        suspendRunCatching {
            val timetableLecturesString = timetableDataStore.getString(semester).firstOrNull().orEmpty()
            val timetableLecturesType = object : TypeToken<TimetableLectures>() {}.type
            try {
                gson.fromJson(timetableLecturesString, timetableLecturesType)
            } catch (e: NullPointerException) {
                TimetableLectures(0, emptyList(), 0, 0)
            }
        }

    override suspend fun putTimetableLectures(lectures: TimetableLecturesQuery): Result<TimetableLectures> =
        suspendRunCatching {
            timetableRemoteDataSource.putTimetableLectures(lectures.toTimetableLecturesQueryRequest()).toTimetableLectures()
        }

    override suspend fun putTimetableLectures(
        key: String,
        value: TimetableLectures
    ): Result<TimetableLectures> =
        suspendRunCatching {
            timetableDataStore.putString(key, gson.toJson(value))
            getTimetableLectures(semester = key).getOrThrow()
        }

    override suspend fun putTimetableFrame(
        id: Int,
        frame: TimetableFrameQuery
    ): Result<TimetableFrame> =
        suspendRunCatching {
            timetableRemoteDataSource
                .putTimetableFrame(
                    id,
                    TimetableFrameQueryRequest(
                        frame.timetableName,
                        frame.isMain
                    )
                ).toTimetableFrame()
        }

    override suspend fun postTimetableLectures(
        frameId: Int,
        lectures: List<Lecture>
    ): Result<TimetableLectures> =
        suspendRunCatching {
            timetableRemoteDataSource
                .postTimetableLectures(
                    LecturesQueryRequest(
                        timetableFrameId = frameId,
                        timetableLecture = lectures.map { it.toLectureQueryRequest() }
                    )
                ).toTimetableLectures()
        }

    override suspend fun postTimetableCustomLectures(
        frameId: Int,
        lectures: List<Lecture>
    ): Result<TimetableLectures> =
        suspendRunCatching {
            val info =
                lectures.map { it.classTime to it.place }.map { (classTime, place) ->
                    TimetableLectureClassInfoRequest(classTime = classTime, classPlace = place)
                }

            val query =
                LectureQueryRequest(
                    classTitle = lectures.firstOrNull()?.name.orEmpty(),
                    classInfos = info,
                    professor = lectures.firstOrNull()?.professor.orEmpty(),
                    lectureId = null,
                    grades = "0",
                    memo = ""
                )

            timetableRemoteDataSource
                .postTimetableLectures(
                    LecturesQueryRequest(
                        timetableFrameId = frameId,
                        timetableLecture = listOf(query)
                    )
                ).toTimetableLectures()
        }

    override suspend fun postTimetableBasicLectures(
        frameId: Int,
        lectures: List<TimetableLecture>
    ): Result<TimetableLectures> =
        suspendRunCatching {
            val queryLectures =
                lectures.map {
                    if (it.lectureId == 0) {
                        it.toCustomLectureQueryRequest()
                    } else {
                        it.toLectureQueryRequest()
                    }
                }

            timetableRemoteDataSource
                .postTimetableLectures(
                    LecturesQueryRequest(
                        timetableFrameId = frameId,
                        timetableLecture = queryLectures
                    )
                ).toTimetableLectures()
        }

    override suspend fun postTimetableFrame(frame: TimetableFrameCreateQuery): Result<TimetableFrame> =
        suspendRunCatching {
            timetableRemoteDataSource
                .postTimetableFrame(
                    TimetableFrameCreateQueryRequest(
                        semester = frame.semester,
                        timetableName = frame.timetableName
                    )
                ).toTimetableFrame()
        }.recoverCatching {
            if (it is HttpException) {
                throw Exception()
            } else {
                throw it
            }
        }

    override suspend fun postRollbackFrame(frameId: Int): Result<TimetableLectures> =
        suspendRunCatching {
            timetableRemoteDataSource.postRollbackFrame(frameId).toTimetableLectures()
        }

    override suspend fun deleteTimetableFrame(frameId: Int): Result<Unit> =
        suspendRunCatching {
            timetableRemoteDataSource.deleteTimetableFrame(frameId)
        }

    override suspend fun deleteTimetableLecture(id: Int): Result<Unit> =
        suspendRunCatching {
            timetableRemoteDataSource.deleteTimetableLecture(id)
        }

    override suspend fun deleteTimetableFrameLecture(
        frameId: Int,
        lectureId: Int
    ): Result<Unit> =
        suspendRunCatching {
            timetableRemoteDataSource.deleteTimetableFrameLecture(frameId, lectureId)
        }

    override suspend fun deleteTimetableLectures(lectureIds: List<Int>): Result<Unit> =
        suspendRunCatching {
            val response = timetableRemoteDataSource.deleteTimetableLectures(lectureIds)
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }

    override suspend fun deleteAllTimetableFrame(semester: String): Result<Unit> =
        suspendRunCatching {
            timetableRemoteDataSource.deleteAllTimetableFrame(semester)
        }
}
