package `in`.koreatech.koin.domain.model.timetable

import `in`.koreatech.koin.domain.util.ext.toDepartmentString
import java.time.DayOfWeek
import java.time.LocalTime

data class Lecture(
    var id: Int = 0,
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
    val classTime: List<Int> = emptyList(),
) {
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

    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            "$name",
            "${name?.first()}"
        )

        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }

    fun doesMatchDepartmentSearchQuery(departments: List<String>): Boolean {
        val matchingCombination = department.toDepartmentString()

        return departments.any {
            it.contains(matchingCombination, ignoreCase = true)
        }
    }

    /**
     * 시간표 강의 중복
     * @example : 강의 시간 겹침 + 완전 준복
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