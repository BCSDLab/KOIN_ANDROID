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
    val classTime: List<Int>
) {
    fun toTimetableLecture() = TimetableLecture(
        id = id,
        lectureId = id,
        regularNumber = regularNumber,
        code = code,
        designScore = designScore,
        classTime = classTime,
        classPlace = "", // Lecture에 없는데 어쩌자고?
        memo = "", // Lecture에 없다고.
        grades = grades,
        classTitle = name, // 데이터 이름이 다른거냐
        lectureClass = lectureClass,
        target = target,
        professor = professor,
        department = department
    )

    /**
     * @reference : LectureTest.kt
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
            "$name",
            "${name?.first()}"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }

    fun doesMatchDepartmentSearchQuery(query: String): Boolean {
        val matchingCombination = query.toDepartmentString()

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

fun String.toDepartmentString(): String = when (this) {
    "HRD학과" -> "HRD"
    "고용서비스정책학과" -> "고용서비스정책학과"
    "교양학부" -> "교양"
    "디자인ㆍ건축공학부" -> "디자인공학부"
    "메카트로닉스공학부" -> "메카트로닉스공학부"
    "산업경영학부" -> "산업경영학부"
    "에너지신소재화학공학부" -> "에너지신소재공학부"
    "융합학과" -> "융합"
    "전기ㆍ전자ㆍ통신공학부" -> "전기"
    "컴퓨터공학부" -> "컴퓨터공학부"
    "안전공학과" -> "안전"
    "기계공학부" -> "기계"
    else -> ""
}
