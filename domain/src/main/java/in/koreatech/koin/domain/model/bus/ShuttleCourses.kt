package `in`.koreatech.koin.domain.model.bus

data class ShuttleCourses(
    val courses: List<ShuttleCourse>,
    val semester: ShuttleSemester
)
