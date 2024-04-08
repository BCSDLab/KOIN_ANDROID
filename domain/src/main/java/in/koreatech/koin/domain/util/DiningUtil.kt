package `in`.koreatech.koin.domain.util

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.domain.util.ext.typeFilter

object DiningUtil {
    private val diningEndTime = listOf("09:00", "13:30", "18:30")

    fun typeFiltering(diningList: List<Dining>, type: DiningType): List<Dining> =
        diningList.typeFilter(type).arrange()

    fun getCurrentType() = if (TimeUtil.compareWithCurrentTime(diningEndTime[2]) >= 0) {
        DiningType.Dinner
    } else if (TimeUtil.compareWithCurrentTime(diningEndTime[1]) >= 0) {
        DiningType.Lunch
    } else if (TimeUtil.compareWithCurrentTime(diningEndTime[0]) >= 0) {
        DiningType.Breakfast
    } else {
        DiningType.Breakfast
    }

    fun isNextDay() = TimeUtil.compareWithCurrentTime(diningEndTime[2]) < 0

    fun getCurrentDate() =
        if (isNextDay()) TimeUtil.getNextDayDate(TimeUtil.getCurrentTime())
        else TimeUtil.getCurrentTime()

    fun findDining(diningList: List<Dining>, type: DiningType, place: String): Dining? {
        diningList.typeFilter(type).forEach {
            if (it.place == place) return it
        }
        return null
    }

    fun sortDiningByPlace(diningList: List<Dining>): List<Dining> {
        val priority = mutableListOf<Int>()
        for(dining in diningList) {
            priority.add(
                when (dining.place) {
                    "A코너" -> 0
                    "B코너" -> 1
                    "C코너" -> 2
                    "능수관" -> 3
                    "2캠퍼스" -> 4
                    else -> 5
                }
            )
        }
        return diningList.sortedWith(compareBy { priority[diningList.indexOf(it)] })
    }
}