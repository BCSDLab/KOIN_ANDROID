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
        val classTimes = timetable.map { it.classTime.map { it % 100 } }
        var maxTime = 0
        classTimes.forEach { times ->
            times.forEach {
                if (maxTime < it) {
                    maxTime = it
                }
            }
        }

        return  if (maxTime in 18..19) {
            10
        } else if (maxTime in 20..21) {
            11
        } else if (maxTime in 22..23) {
            12
        } else if (maxTime in 24..25) {
            13
        } else if (maxTime in 26..27) {
            14
        } else if (maxTime in 28..29) {
            15
        } else {
            9
        }
    }
}

