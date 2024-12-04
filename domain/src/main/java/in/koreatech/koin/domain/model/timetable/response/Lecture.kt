package `in`.koreatech.koin.domain.model.timetable.response

import java.time.DayOfWeek
import java.time.LocalTime

data class Lecture(
    val id: Int,
    val code: String = "",
    val name: String = "",
    val grades: String = "",
    val lectureClass: String = "",
    val regularNumber: String = "",
    val department: String = "",
    val target: String = "",
    val professor: String = "",
    val isEnglish: String = "",
    val designScore: String = "",
    val isElearning: String = "",
    val classTime: List<Int>,
    val place: String? = null
) {
    fun toTimetableLecture() = TimetableLecture(
        id = id,
        lectureId = id,
        regularNumber = regularNumber,
        code = code,
        designScore = designScore,
        classInfos = listOf(
            TimetableLectureClassInfo(
                classTime = classTime,
                classPlace = place ?: ""
            )
        ),
        memo = "",
        grades = grades,
        classTitle = name,
        lectureClass = lectureClass,
        target = target,
        professor = professor,
        department = department
    )

    /**
     * @test : LectureTest.kt
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

    fun formatDescription(): String {
        val description = if (grades.isEmpty()) {
            ""
        } else {
            ""
        }.let {
            if (grades.isNotEmpty()) it + "${grades}학점"
            else it
        }.let {
            if (code.isNotEmpty()) "$it  $code"
            else it
        }

        return description
    }

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
            name.take(0),
            professor,
            professor.take(0),
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }

    fun doesMatchDepartmentSearchQuery(query: String): Boolean {
        val matchingCombination = query

        return department.contains(matchingCombination, ignoreCase = true)
    }

    /**
     * 시간표 강의 중복
     * @example : 강의 시간 겹침 + 완전 중복
     */
    fun duplicate(lectures: List<Lecture>): Boolean {
        var flag = false
        classTime.forEach { time ->
            if (lectures.filter { it.classTime.contains(time) }.isNotEmpty()) {
                flag = true
            }
        }
        return flag
    }
}
