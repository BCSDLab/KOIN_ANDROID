package `in`.koreatech.koin.domain.util

import `in`.koreatech.koin.domain.model.dining.DiningType

object DiningUtil {
    private val diningEndTime = listOf("09:00", "13:30", "18:30")

    fun getCurrentType() = if (TimeUtil.compareWithCurrentTime(diningEndTime[0]) >= 0) {
        DiningType.Breakfast
    } else if (TimeUtil.compareWithCurrentTime(diningEndTime[1]) >= 0) {
        DiningType.Lunch
    } else if (TimeUtil.compareWithCurrentTime(diningEndTime[2]) >= 0) {
        DiningType.Dinner
    } else {
        DiningType.Breakfast
    }

    fun isNextDay() = TimeUtil.compareWithCurrentTime(diningEndTime[2]) < 0
}