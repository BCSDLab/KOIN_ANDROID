package `in`.koreatech.koin.ui.timetablev2

import `in`.koreatech.koin.domain.model.timetable.Semester

data class TimetableState(
    val semesters: List<Semester> = emptyList()
)
