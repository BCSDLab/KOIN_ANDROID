package `in`.koreatech.koin.data.mapper

import com.google.gson.annotations.SerializedName
import `in`.koreatech.koin.data.request.timetable.TimetablesLectureRequest
import `in`.koreatech.koin.data.request.timetable.TimetablesRequest
import `in`.koreatech.koin.data.response.timetable.DepartmentResponse
import `in`.koreatech.koin.data.response.timetable.LectureResponse
import `in`.koreatech.koin.data.response.timetable.SemesterResponse
import `in`.koreatech.koin.data.response.timetable.TimetablesLectureResponse
import `in`.koreatech.koin.domain.model.timetable.Department
import `in`.koreatech.koin.domain.model.timetable.Lecture
import `in`.koreatech.koin.domain.model.timetable.Semester

fun SemesterResponse.toSemester() = Semester(
    id = this.id,
    semester = this.semester
)

fun LectureResponse.toLecture() = Lecture(
    id = this.id ?: 0,
    code = this.code ?: "",
    name = this.name ?: "",
    grades = this.grades ?: "",
    lectureClass = this.lectureClass ?: "",
    regularNumber = this.regularNumber ?: "",
    department = this.department ?: "",
    target = this.target ?: "",
    professor = this.professor ?: "",
    isEnglish = this.isEnglish ?: "",
    designScore = this.designScore ?: "",
    isElearning = this.isElearning ?: "",
    classTime = this.classTime,
)

fun TimetablesLectureResponse.toLecture() = Lecture(
    id = this.id ?: 0,
    code = this.code ?: "",
    name = this.name ?: "",
    grades = this.grades ?: "",
    lectureClass = this.lectureClass ?: "",
    regularNumber = this.regularNumber ?: "",
    department = this.department ?: "",
    target = this.target ?: "",
    professor = this.professor ?: "",
    isEnglish = "",
    designScore = this.designScore ?: "",
    isElearning = "",
    classTime = this.classTime.orEmpty(),
)

fun DepartmentResponse.toDepartment() = Department(
    id = this.id,
    name = this.name
)

fun List<Lecture>.toTimetablesRequest(semester: String) = TimetablesRequest(
    timetable = this.map { it.toTimetablesLectureResponse() },
    semester = semester
)

fun Lecture.toTimetablesLectureResponse() = TimetablesLectureRequest(
    id = this.id,
    code = this.code,
    name = this.name,
    grades = this.grades,
    lectureClass = this.lectureClass,
    regularNumber = this.regularNumber,
    department = this.department,
    target = this.target,
    professor = this.professor,
    designScore = this.designScore,
    classPlace = "",
    memo = "",
    classTime = this.classTime
)