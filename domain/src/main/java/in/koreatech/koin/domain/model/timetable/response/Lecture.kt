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
