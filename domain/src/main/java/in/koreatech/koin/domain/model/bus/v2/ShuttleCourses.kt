package `in`.koreatech.koin.domain.model.bus.v2

data class ShuttleCourses(
    val courses: List<ShuttleCourse>,
    val semester: ShuttleSemester
)
