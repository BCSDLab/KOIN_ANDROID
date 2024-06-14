package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import `in`.koreatech.koin.domain.model.timetable.Semester

fun SemesterResponse.toSemester() = Semester(
    id = this.id,
    semester = this.semester
)