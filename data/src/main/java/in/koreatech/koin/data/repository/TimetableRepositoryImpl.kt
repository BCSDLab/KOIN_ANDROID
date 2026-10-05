package `in`.koreatech.koin.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.koreatech.koin.data.mapper.toCustomLectureCreateBody
import `in`.koreatech.koin.data.mapper.toCustomLectureUpdateBody
import `in`.koreatech.koin.data.mapper.toLecture
import `in`.koreatech.koin.data.mapper.toRegularLectureBody
import `in`.koreatech.koin.data.mapper.toSemesterFrames
import `in`.koreatech.koin.data.mapper.toTimeRanges
import `in`.koreatech.koin.data.mapper.toTimetableFrame
import `in`.koreatech.koin.data.mapper.toTimetableLectures
import `in`.koreatech.koin.data.mapper.toYearTerm
import `in`.koreatech.koin.data.request.timetable.v3.TimetableCustomLectureRequest
import `in`.koreatech.koin.data.request.timetable.v3.TimetableCustomLectureRequest.TimetableCustomLectureBody
import `in`.koreatech.koin.data.request.timetable.v3.TimetableCustomLectureRequest.TimetableCustomLectureInfo
import `in`.koreatech.koin.data.request.timetable.v3.TimetableFrameCreateRequestV3
import `in`.koreatech.koin.data.request.timetable.v3.TimetableFrameUpdateRequestV3
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureCreateRequest
import `in`.koreatech.koin.data.request.timetable.v3.TimetableRegularLectureRequest
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
            val (year, term) = semesterDate.toYearTerm()
            emit(timetableRemoteDataSource.getLectures(year, term).map { it.toLecture() })
        }

    override fun getTimetableFrames(semester: String): Flow<List<TimetableFrame>> =
        flow {
            val (year, term) = semester.toYearTerm()
            emit(timetableRemoteDataSource.getTimetableFrames(year, term).map { it.toTimetableFrame() })
        }

    override fun getAllFrames(): Flow<Map<String, List<TimetableFrame>>> =
        flow {
            emit(timetableRemoteDataSource.getAllFrames().toSemesterFrames())
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
            } catch (_: NullPointerException) {
                TimetableLectures(0, emptyList(), 0, 0)
            }
        }

    override suspend fun putTimetableLectures(lectures: TimetableLecturesQuery): Result<TimetableLectures> =
        suspendRunCatching {
            val frameId = lectures.timetableFrameId
            lectures.timetableLecture.map {
                if (it.lectureId == 0) {
                    timetableRemoteDataSource.putTimetableCustomLecture(
                        TimetableCustomLectureRequest(frameId, it.toCustomLectureUpdateBody())
                    )
                } else {
                    timetableRemoteDataSource.putTimetableRegularLecture(
                        TimetableRegularLectureRequest(frameId, it.toRegularLectureBody())
                    )
                }
            }.last().toTimetableLectures()
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
                    TimetableFrameUpdateRequestV3(
                        frame.timetableName,
                        frame.isMain
                    )
                )
                .first { it.id == id }
                .toTimetableFrame()
        }

    override suspend fun postTimetableLectures(
        frameId: Int,
        lectures: List<Lecture>
    ): Result<TimetableLectures> =
        suspendRunCatching {
            lectures.map {
                timetableRemoteDataSource.postTimetableRegularLecture(
                    TimetableRegularLectureCreateRequest(
                        timetableFrameId = frameId,
                        lectureId = it.id
                    )
                )
            }.last().toTimetableLectures()
        }

    override suspend fun postTimetableCustomLectures(
        frameId: Int,
        lectures: List<Lecture>
    ): Result<TimetableLectures> =
        suspendRunCatching {
            val info =
                lectures.flatMap { lecture ->
                    lecture.classTime.toTimeRanges().map { range ->
                        TimetableCustomLectureInfo(
                            startTime = range.startTime,
                            endTime = range.endTime,
                            place = lecture.place
                        )
                    }
                }

            val query =
                TimetableCustomLectureBody(
                    id = null,
                    classTitle = lectures.firstOrNull()?.name.orEmpty(),
                    lectureInfos = info,
                    professor = lectures.firstOrNull()?.professor.orEmpty(),
                    grades = "0",
                    memo = ""
                )

            timetableRemoteDataSource
                .postTimetableCustomLecture(
                    TimetableCustomLectureRequest(
                        timetableFrameId = frameId,
                        timetableCustomLectureBody = query
                    )
                ).toTimetableLectures()
        }

    override suspend fun postTimetableBasicLectures(
        frameId: Int,
        lectures: List<TimetableLecture>
    ): Result<TimetableLectures> =
        suspendRunCatching {
            lectures.map {
                if (it.lectureId == 0) {
                    timetableRemoteDataSource.postTimetableCustomLecture(
                        TimetableCustomLectureRequest(frameId, it.toCustomLectureCreateBody())
                    )
                } else {
                    timetableRemoteDataSource.postTimetableRegularLecture(
                        TimetableRegularLectureCreateRequest(frameId, it.lectureId)
                    )
                }
            }.last().toTimetableLectures()
        }

    override suspend fun postTimetableFrame(frame: TimetableFrameCreateQuery): Result<TimetableFrame> =
        suspendRunCatching {
            val (year, term) = frame.semester.toYearTerm()
            timetableRemoteDataSource
                .postTimetableFrame(
                    TimetableFrameCreateRequestV3(
                        year = year,
                        term = term
                    )
                ).maxBy { it.id }
                .toTimetableFrame()
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
            val (year, term) = semester.toYearTerm()
            timetableRemoteDataSource.deleteAllTimetableFrame(year, term)
        }
}
