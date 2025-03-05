package `in`.koreatech.koin.domain.model.timetable.response

data class TimetableLectures(
    val timetableFrameId: Int,
    val timetable: List<TimetableLecture>,
    val grades: Int,
    val totalGrades: Int,
) {
    /**
     * @test : TimetableLecturesTest.kt
     */
    fun formatTimeRange(): Int {
        val maxTime =
            timetable.flatMap { it.classInfos }
                .flatMap { it.classTime }
                .maxOfOrNull { it % 100 } ?: 0

        return when (maxTime) {
            in 18..19 -> 10
            in 20..21 -> 11
            in 22..23 -> 12
            in 24..25 -> 13
            in 26..27 -> 14
            in 28..29 -> 15
            else -> 9
        }
    }
}
