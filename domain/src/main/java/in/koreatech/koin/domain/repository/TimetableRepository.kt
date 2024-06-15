package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {
    suspend fun getTimetables(key: String): List<Lecture>
    suspend fun <T> putString(key: String, value: T)
    suspend fun loadSemesters(): List<Semester>
    suspend fun loadDepartments(): List<Department>
    suspend fun loadLectures(semester: String): List<Lecture>
}