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
    fun findDayOfWeekAndLocalTime(): List<Pair<DayOfWeek?, List<LocalTime>>> {
        fun groupConsecutiveNumbers(numbers: List<Int>): List<List<Int>> {
            if (numbers.isEmpty()) return emptyList()

            val grouped = mutableListOf<MutableList<Int>>()
            var currentGroup = mutableListOf(numbers[0])

            for (i in 1 until numbers.size) {
                if (numbers[i] == numbers[i - 1] + 1) {
                    currentGroup.add(numbers[i])
                } else {
                    grouped.add(currentGroup)
                    currentGroup = mutableListOf(numbers[i])
                }
            }
            grouped.add(currentGroup)
            return grouped
        }

        fun getLocalTimeGroup(group: List<Int>): List<LocalTime> {
            return group.map {
                val time = it % 100
                LocalTime.of(9 + time / 2, (time % 2) * 30)
            }
        }

        fun getDayOfWeek(key: Int): DayOfWeek? {
            return when (key) {
                0 -> DayOfWeek.MONDAY
                1 -> DayOfWeek.TUESDAY
                2 -> DayOfWeek.WEDNESDAY
                3 -> DayOfWeek.THURSDAY
                4 -> DayOfWeek.FRIDAY
                else -> null
            }
        }

        val groupedByPrefix = classTime.groupBy { it / 100 }
        val result = mutableListOf<Pair<DayOfWeek?, List<LocalTime>>>()
        for ((key, values) in groupedByPrefix) {
            val consecutiveGroups = groupConsecutiveNumbers(values.sorted())
            for (group in consecutiveGroups) {
                result.add(Pair(getDayOfWeek(key), getLocalTimeGroup(group)))
            }
        }

        return result
    }
}
