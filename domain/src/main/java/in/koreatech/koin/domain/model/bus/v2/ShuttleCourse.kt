package `in`.koreatech.koin.domain.model.bus.v2

data class ShuttleCourse(
    val region: String,
    val routes: List<ShuttleCourseRoute>
)