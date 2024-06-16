package `in`.koreatech.koin.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import `in`.koreatech.koin.data.mapper.toDepartment
import `in`.koreatech.koin.data.mapper.toLecture
import `in`.koreatech.koin.data.mapper.toSemester
import `in`.koreatech.koin.data.mapper.toTimetablesRequest
import `in`.koreatech.koin.data.source.local.TimetableLocalDataSource
import `in`.koreatech.koin.data.source.remote.TimetableRemoteDataSource
import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester
import `in`.koreatech.koin.domain.repository.TimetableRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TimetableRepositoryImpl @Inject constructor(
    private val timetableRemoteDataSource: TimetableRemoteDataSource,
    private val timetableLocalDataSource: TimetableLocalDataSource,
) : TimetableRepository {
    override suspend fun getTimetables(key: String, isAnonymous: Boolean): List<Lecture> =
        try {
            if (isAnonymous) {
                val lectureString = timetableLocalDataSource.getString(key).first()
                val lectureType = object : TypeToken<List<Lecture>>() {}.type
                val gson = Gson()
                val updateLectures =
                    gson.fromJson<List<Lecture>>(lectureString, lectureType).orEmpty()
                updateLectures
            } else {
                timetableRemoteDataSource.loadTimetables(key).timetables?.map { it.toLecture() }.orEmpty()
            }
        } catch (e: Exception) {
            emptyList()
        }

    override suspend fun updateTimetables(key: String, isAnonymous: Boolean, value: List<Lecture>) {
        try {
            if (isAnonymous) {
                timetableLocalDataSource.putString(key, Gson().toJson(value))
            } else {
                timetableRemoteDataSource.updateTimetables(value.toTimetablesRequest(key))
            }
        } catch (e: Exception) {
            e.message
        }
    }

    override suspend fun removeTimetables(id: Int) {
        try {
            timetableRemoteDataSource.deleteTimetables(id)
        } catch (e: Exception) {
            e.message
        }
    }

    override suspend fun loadSemesters(): List<Semester> =
        timetableRemoteDataSource.loadSemesters().map { it.toSemester() }

    override suspend fun loadLectures(semester: String): List<Lecture> =
        timetableRemoteDataSource.loadLectures(semester).map { it.toLecture() }

    override suspend fun loadDepartments(): List<Department> =
        timetableLocalDataSource.loadDepartments()?.map { it.toDepartment() }.orEmpty()
}