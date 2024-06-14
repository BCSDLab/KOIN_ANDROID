package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toSemester
import `in`.koreatech.koin.data.source.remote.TimetableRemoteDataSource
import `in`.koreatech.koin.domain.model.timetable.Semester
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class TimetableRepositoryImpl @Inject constructor(
    private val timetableRemoteDataSource: TimetableRemoteDataSource,
) : TimetableRepository {
    override suspend fun loadSemesters(): List<Semester> =
        timetableRemoteDataSource.loadSemesters().map { it.toSemester() }
}