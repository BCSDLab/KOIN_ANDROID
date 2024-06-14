package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester

interface TimetableRepository {
    suspend fun loadSemesters(): List<Semester>
    suspend fun loadDepartments(): List<Department>
    suspend fun loadLectures(semester: String): List<Lecture>
}