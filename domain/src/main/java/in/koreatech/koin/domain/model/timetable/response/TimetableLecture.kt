package `in`.koreatech.koin.domain.model.timetable.response

import java.time.DayOfWeek
import java.time.LocalTime

data class TimetableLecture(
    val id: Int,
    val lectureId: Int,
    val regularNumber: String = "",
    val code: String = "",
    val designScore: String = "",
    val classTime: List<Int>,
    val classPlace: String = "",
    val memo: String = "",
    val grades: String = "",
    val classTitle: String = "",
    val lectureClass: String = "",
    val target: String = "",
    val professor: String = "",
    val department: String = "",
) {
    /**
     * @reference : TimetableLectureTest.kt
     */
    fun findDayOfWeekAndTime(): Map<DayOfWeek?, List<LocalTime>> {
        return classTime.groupBy { it / 100 }
            .mapValues { entry ->
                /**
                 * @input : [0,1,100,101]
                 */
                entry.value.sorted().map { value ->
                    val timeIndex = if (entry.key == 0) value else value % (entry.key * 100)
                    LocalTime.of(9 + timeIndex / 2, (timeIndex % 2) * 30)
                }
                /**
                 * @output : [09:00, 09:30], [09:00, 09:30]
                 */
            }
            .mapKeys {
                /**
                 * @input : {0=[09:00, 09:30], 1=[09:00, 09:30]}
                 */
                when (it.key) {
                    0 -> DayOfWeek.MONDAY
                    1 -> DayOfWeek.TUESDAY
                    2 -> DayOfWeek.WEDNESDAY
                    3 -> DayOfWeek.THURSDAY
                    4 -> DayOfWeek.FRIDAY
                    else -> null
                }
                /**
                 * @output : {MONDAY=[09:00, 09:30], TUESDAY=[09:00, 09:30]}
                 */
            }
    }
}
