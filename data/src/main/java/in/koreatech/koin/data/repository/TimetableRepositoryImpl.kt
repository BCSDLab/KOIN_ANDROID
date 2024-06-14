package `in`.koreatech.koin.data.repository

import `in`.koreatech.koin.data.mapper.toDepartment
import `in`.koreatech.koin.data.mapper.toLecture
import `in`.koreatech.koin.data.mapper.toSemester
import `in`.koreatech.koin.data.source.local.TimetableLocalDataSource
import `in`.koreatech.koin.data.source.remote.TimetableRemoteDataSource
import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester
import `in`.koreatech.koin.domain.repository.TimetableRepository
import javax.inject.Inject

class TimetableRepositoryImpl @Inject constructor(
    private val timetableRemoteDataSource: TimetableRemoteDataSource,
    private val timetableLocalDataSource: TimetableLocalDataSource,
) : TimetableRepository {
    override suspend fun loadSemesters(): List<Semester> =
        timetableRemoteDataSource.loadSemesters().map { it.toSemester() }

    override suspend fun loadLectures(semester: String): List<Lecture> =
        timetableRemoteDataSource.loadLectures(semester).map { it.toLecture() }

    override suspend fun loadDepartments(): List<Department> =
        timetableLocalDataSource.loadDepartments()?.map { it.toDepartment() }.orEmpty()
}