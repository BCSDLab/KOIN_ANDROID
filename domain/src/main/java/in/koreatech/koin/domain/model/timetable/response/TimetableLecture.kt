package `in`.koreatech.koin.domain.model.timetable.response

import java.time.DayOfWeek
import java.time.LocalTime

data class TimetableLecture(
    val id: Int,
    val lectureId: Int,
    val regularNumber: String = "",
    val code: String = "",
    val designScore: String = "",
    val classInfos: List<TimetableLectureClassInfo> = emptyList(),
    val memo: String = "",
    val grades: String = "",
    val classTitle: String = "",
    val lectureClass: String = "",
    val target: String = "",
    val professor: String = "",
    val department: String = "",
) {
    /**
     * @test : TimetableLectureTest.kt
     */
    fun formatTimetableEventContent(): List<Triple<DayOfWeek?, List<LocalTime>, String>> {
        return classInfos.flatMap { classInfo ->
            classInfo.classTime.groupBy { it / 100 }.flatMap { (dayOfWeekKey, times) ->
                groupConsecutiveTimes(times.sorted()).map { timeGroup ->
                    Triple(dayOfWeekKey.toDayOfWeek(), timeGroup.toLocalTimes(), classInfo.classPlace)
                }
            }
        }
    }

    private fun groupConsecutiveTimes(times: List<Int>): List<List<Int>> {
        if (times.isEmpty()) return emptyList()

        val grouped = mutableListOf<MutableList<Int>>()
        var currentGroup = mutableListOf(times[0])

        for (i in 1 until times.size) {
            if (times[i] == times[i - 1] + 1) {
                currentGroup.add(times[i])
            } else {
                grouped.add(currentGroup)
                currentGroup = mutableListOf(times[i])
            }
        }
        grouped.add(currentGroup)
        return grouped
    }

    private fun Int.toDayOfWeek(): DayOfWeek? = when(this) {
        0 -> DayOfWeek.MONDAY
        1 -> DayOfWeek.TUESDAY
        2 -> DayOfWeek.WEDNESDAY
        3 -> DayOfWeek.THURSDAY
        4 -> DayOfWeek.FRIDAY
        else -> null
    }

    private fun List<Int>.toLocalTimes(): List<LocalTime> = map {
        val time = it % 100
        LocalTime.of(9 + time / 2, (time % 2) * 30)
    }

    fun getDetailTime(): String {
        val times = formatTimetableEventContent()

        val timeContent = StringBuilder()

        times.forEachIndexed { index, (dayOfWeekContent, localTimes, place) ->
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
