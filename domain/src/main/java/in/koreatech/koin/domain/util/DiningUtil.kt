package `in`.koreatech.koin.domain.util

import `in`.koreatech.koin.domain.model.dining.Dining
import `in`.koreatech.koin.domain.model.dining.DiningType
import `in`.koreatech.koin.domain.util.ext.arrange
import `in`.koreatech.koin.domain.util.ext.typeFilter

object DiningUtil {
    private val diningEndTime = listOf("09:00", "13:30", "18:30", "23:59")

    fun typeFiltering(diningList: List<Dining>, type: DiningType): List<Dining> =
        diningList.typeFilter(type).arrange()

    fun getTypeByString(type: String): DiningType {
        return when (type) {
            DiningType.Breakfast.typeEnglish -> DiningType.Breakfast
            DiningType.Lunch.typeEnglish -> DiningType.Lunch
            DiningType.Dinner.typeEnglish -> DiningType.Dinner
            DiningType.NextBreakfast.typeEnglish -> DiningType.NextBreakfast
            else -> DiningType.Breakfast
        }
    }


    fun getCurrentType(): DiningType {
        var currentType = DiningType.Breakfast
        diningEndTime.forEachIndexed { index, time ->
            if (TimeUtil.compareWithCurrentTime(time) >= 0) {
                currentType = when (index) {
                    0 -> DiningType.Breakfast
                    1 -> DiningType.Lunch
                    2 -> DiningType.Dinner
                    3 -> DiningType.NextBreakfast
                    else -> DiningType.Breakfast
                }
                return currentType
            }
        }
        return currentType
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

    fun getKoreanName(type: String): String {
        return when (type) {
            DiningType.Breakfast.typeEnglish -> DiningType.Breakfast.typeKorean
            DiningType.Lunch.typeEnglish -> DiningType.Lunch.typeKorean
            DiningType.Dinner.typeEnglish -> DiningType.Dinner.typeKorean
            DiningType.NextBreakfast.typeEnglish -> DiningType.NextBreakfast.typeKorean
            else -> "Unknown type"
        }
    }
}