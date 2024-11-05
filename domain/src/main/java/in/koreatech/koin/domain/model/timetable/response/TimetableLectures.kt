package `in`.koreatech.koin.domain.model.timetable.response

data class TimetableLectures(
    val timetableFrameId: Int,
    val timetable: List<TimetableLecture>,
    val grades: Int,
    val totalGrades: Int,
){
    /**
     * @reference : TimetableLecturesTest.kt 파일 참고
     */
    fun formatTimeRange(): Int {
        val a = timetable.map { it.classTime }
        var maxItem = 0
        a.forEach {items ->
            items.forEach {
                if (maxItem < it) {
                    maxItem = it
                }
            }
        }

        val range = (maxItem % 100) - 9

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

