package `in`.koreatech.koin.domain.repository

import `in`.koreatech.koin.domain.model.timetable.Semester

interface TimetableRepository {
    suspend fun loadSemesters(): List<Semester>
}