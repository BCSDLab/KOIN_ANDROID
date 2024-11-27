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

        fun splitClassTime(): List<List<Int>> {
            return classTime.fold(mutableListOf<MutableList<Int>>()) { acc, num ->
                if (num == -1) {
                    acc.add(mutableListOf())
                } else {
                    if (acc.isEmpty()) {
                        acc.add(mutableListOf(num))
                    } else {
                        acc.last().add(num)
                    }
                }
                acc
            }.filter { it.isNotEmpty() }
        }

        if (classTime.contains(-1)) {
            val classTimes = splitClassTime()
            val result = mutableListOf<Pair<DayOfWeek?, List<LocalTime>>>()

            classTimes.forEach { times ->
                val groupedByPrefix = times.groupBy { it / 100 }

                for ((key, values) in groupedByPrefix) {
                    val consecutiveGroups = groupConsecutiveNumbers(values.sorted())
                    for (group in consecutiveGroups) {
                        result.add(Pair(getDayOfWeek(key), getLocalTimeGroup(group)))
                    }
                }

            }

            return result
        } else {
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

    fun getDetailTime(): String {
        val times = findDayOfWeekAndLocalTime()

        val timeContent = StringBuilder()

        times.forEachIndexed { index, (dayOfWeekContent, localTimes) ->
            if (index > 0) timeContent.append(", ")
            val dayOfWeekText = when (dayOfWeekContent) {
                DayOfWeek.MONDAY -> "월"
                DayOfWeek.TUESDAY -> "화"
                DayOfWeek.WEDNESDAY -> "수"
                DayOfWeek.THURSDAY -> "목"
                DayOfWeek.FRIDAY -> "금"
                else -> ""
            }
            timeContent.append(dayOfWeekText)

            fun localTimeToString(localTime: LocalTime): String {
                val hour = if (localTime.hour - 8 < 10) {
                    "0${(localTime.hour - 8)}"
                } else {
                    (localTime.hour - 8).toString()
                }
                val minutes = if (localTime.minute == 0) "A" else "B"
                return hour + minutes
            }

            if (localTimes.isNotEmpty()) {
                localTimes.firstOrNull()?.let {
                    timeContent.append(localTimeToString(it))
                }

                localTimes.lastOrNull()?.let {
                    timeContent.append("~")
                    timeContent.append(localTimeToString(it))
                }
            }
        }

        return timeContent.toString()
    }
}
