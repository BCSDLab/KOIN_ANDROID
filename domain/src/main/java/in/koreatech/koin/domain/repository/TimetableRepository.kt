package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester
import kotlinx.coroutines.flow.Flow

interface TimetableRepository {
    suspend fun getTimetables(key: String, isAnonymous: Boolean): List<Lecture>
    suspend fun updateTimetables(key: String, isAnonymous: Boolean, value: List<Lecture>)
    suspend fun removeTimetables(id: Int)
    suspend fun loadSemesters(): List<Semester>
    suspend fun loadDepartments(): List<Department>
    suspend fun loadLectures(semester: String): List<Lecture>
}