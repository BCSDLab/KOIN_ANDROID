package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.source.remote.TimetableRemoteDataSource
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameCreateQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableFrameQuery
import `in`.koreatech.koin.domain.model.timetable.request.TimetableLecturesQuery
import `in`.koreatech.koin.domain.model.timetable.response.Lecture
import `in`.koreatech.koin.domain.model.timetable.response.Semester
import `in`.koreatech.koin.domain.model.timetable.response.TimetableFrame
import `in`.koreatech.koin.domain.model.timetable.response.TimetableLectures
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class TimetableRepositoryImpl @Inject constructor(
    private val timetableRemoteDataSource: TimetableRemoteDataSource
): TimetableRepository {
    override suspend fun getSemesters(): List<Semester> {
        TODO("Not yet implemented")
    }

    override suspend fun getLectures(semesterDate: String): List<Lecture> {
        TODO("Not yet implemented")
    }

    override suspend fun getTimetableLectures(): TimetableLectures {
        TODO("Not yet implemented")
    }

    override suspend fun putTimetableLectures(lectures: TimetableLecturesQuery): TimetableLectures {
        TODO("Not yet implemented")
    }

    override suspend fun postTimetableLectures(lectures: TimetableLecturesQuery): TimetableLectures {
        TODO("Not yet implemented")
    }

    override suspend fun putTimetableFrame(id: Int, frame: TimetableFrameQuery): TimetableFrame {
        TODO("Not yet implemented")
    }

    override suspend fun postTimetableFrame(frame: TimetableFrameCreateQuery): TimetableFrame {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTimetableFrame() {
        TODO("Not yet implemented")
    }

    override suspend fun getTimetableFrames(semester: String): List<TimetableFrame> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTimetableLecture(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTimetableFrame() {
        TODO("Not yet implemented")
    }
}