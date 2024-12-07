package `in`.koreatech.koin.domain.model.bus.v2

data class ShuttleCourses(
    val courses: List<ShuttleCourse>,
    val semester: ShuttleSemester
)

data class ShuttleCourse(
    val region: String,
    val routes: List<ShuttleCourseRoute>
)

data class ShuttleCourseRoute(
    val id: String,
    val routeName: String,
    val subName: String,
)

data class ShuttleSemester(
    val name: String,
    val term: String,
)
