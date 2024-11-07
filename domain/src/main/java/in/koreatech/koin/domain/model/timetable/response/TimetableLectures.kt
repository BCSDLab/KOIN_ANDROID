package `in`.koreatech.koin.domain.model.timetable.response

data class TimetableLectures(
    val timetableFrameId: Int,
    val timetable: List<TimetableLecture>,
    val grades: Int,
    val totalGrades: Int,
) {
    /**
     * @reference : TimetableLecturesTest.kt 파일 참고
     */
    fun formatTimeRange(): Int {
        val classTimes = timetable.map { it.classTime }
        var maxTime = 0
        classTimes.forEach { times ->
            times.forEach {
                if (maxTime < it) {
                    maxTime = it
                }
            }
        }

        val range = (maxTime % 100) - 9

        return if (range < 9) {
            9
        } else {
            if (range % 2 == 1) {
                range + 1
            } else {
                range
            }
        }
    }
}

